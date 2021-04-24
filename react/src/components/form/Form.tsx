import React from 'react';
import { useMainAreaContext } from '../editor/MainAreaProvider';
import { useSidebarContext } from '../navigation/sidebar/SidebarProvider';

interface CustomFormProps {
    initialValues: any;
    validations: any;

    onSubmit?(v: any): Promise<void>

    redirectAfterSave?(): void;
}

interface Values {
    [key: string]: any;
}

interface Errors {
    [key: string]: any;
}

interface Touched {
    [key: string]: boolean;
}

interface FormValidation {
    values: Values;
    errors: Errors;
    touched: Touched;
}

interface ChangeRequest {
    name: string;
    value: any;
    type?: string;
}

const useCustomForm = ({ initialValues, validations, onSubmit, redirectAfterSave }: CustomFormProps) => {
    const [initial, setInitial] = React.useState<Values>(initialValues || {});
    const [values, setValues] = React.useState<Values>(initialValues || {});
    const [errors, setErrors] = React.useState<Errors>({});
    const [touched, setTouched] = React.useState<Touched>({});
    const [isFormSaved, setFormSaved] = React.useState<boolean>(false);
    const { setShouldAskBeforeLeave } = useMainAreaContext();
    const {setRefreshExplorerPanel} = useSidebarContext();

    React.useEffect(() => {
        if (isFormSaved && redirectAfterSave) {
            redirectAfterSave();
        }
    }, [isFormSaved]);

    React.useEffect(() => {
        if (Object.keys(touched).length !== 0) {
            setShouldAskBeforeLeave(true);
        }
    }, [touched]);

    React.useEffect(() => {
        if (JSON.stringify(initial) !== JSON.stringify(initialValues)) {
            setValues(initialValues);
            setErrors({});
            setTouched({});
            setInitial(initialValues);
        }
    }, [initialValues]);

    const getNewStateChange = ({name, value, type}: ChangeRequest, prevState?: FormValidation): FormValidation => {
        const s = prevState ? prevState : {values: values, errors: errors, touched: touched};

        const tp = type ? type : 'string';
        // keep number fields as numbers
        const newValue = tp === 'number' ? +value : value;
        const error = validations[name] ? validations[name](newValue, s.values) : '';
        const {[name]: removedError, ...rest} = s.errors;

        const v: Values = {...s.values, [name]: newValue};
        const t: Touched = {...s.touched, [name]: true};
        const e: Errors = {...rest, ...(error && {[name]: error})};

        return {values: v, touched: t, errors: e};
    };

    const handleChange = (event: React.ChangeEvent<HTMLInputElement> | React.ChangeEvent<HTMLTextAreaElement> | React.ChangeEvent<HTMLSelectElement>) => {
        setFormValidation(getNewStateChange({name: event.target.name, value: event.target.value, type: event.target.type}));
    };

    const handleMultipleChange = (changeRequests: ChangeRequest[]): void => {
        let prevState: FormValidation | undefined;
        let currentState: FormValidation | undefined;
        changeRequests.map((c) => {
            prevState = currentState;
            currentState = getNewStateChange(c, prevState);
        });
        if (currentState) {
            setFormValidation(currentState);
        }
    };

    const setFormValidation = (formValidation: FormValidation): void => {
        setValues(formValidation.values);
        setErrors(formValidation.errors);
        setTouched(formValidation.touched);
    };

    const handleBlur = (event: React.ChangeEvent<HTMLInputElement> | React.ChangeEvent<HTMLTextAreaElement>) => {
        const { name, value } = event.target;
        // remove whatever error was there previously
        const {[name]: removedError, ...rest} = errors;
        const error = validations[name] ? validations[name](value, values) : '';

        // validate the field if the value has been touched
        setErrors({ ...rest, ...(error && { [name]: touched[name] && error })});
    };

    const validateForm = () => {
        const formValidation =  Object.keys(values).reduce(
            (acc, key) => {
                const newError = validations[key] ? validations[key](values[key], values) : '';
                const newTouched = { [key]: true };
                return {
                    errors: {
                        ...acc.errors,
                        ...(newError && { [key]: newError })
                    },
                    touched: {
                        ...acc.touched,
                        ...newTouched
                    }
                };
            },
            {
                errors: { ...errors },
                touched: { ...touched }
            }
        );
        setErrors(formValidation.errors);
        setTouched(formValidation.touched);

        return (
            !Object.values(formValidation.errors).length && // errors object is empty
            Object.values(formValidation.touched).length ===
            Object.values(values).length && // all fields were touched
            Object.values(formValidation.touched).every(t => t) // every touched field is true
        );
    };

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        if (validateForm() && onSubmit) {
            onSubmit({ values }).then(async () => {
                setShouldAskBeforeLeave(false);
                setRefreshExplorerPanel();
                setTouched({});
                setFormSaved(true);
            });
        }
    };

    return {
        values,
        errors,
        touched,
        handleChange,
        handleMultipleChange,
        handleBlur,
        handleSubmit
    };
};

export default useCustomForm;
