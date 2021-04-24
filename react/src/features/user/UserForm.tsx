import { Select, TextInput } from 'equisoft-design-ui-elements';
import React, { ChangeEvent } from 'react';
import { useTranslation } from 'react-i18next';
import { toast } from 'react-toastify';
import useCustomForm from '../../components/form/Form';
import CancelAndSaveButton from '../../components/general/button/CancelAndSaveButtons';
import { defaultFeatureService, defaultLanguageService, defaultOrganizationService, defaultRoleService, defaultUserService } from '../../lib/context';
import BaseEnum from '../../lib/domain/entities/BaseEnum';
import Feature from '../../lib/domain/entities/Feature';
import Language from '../../lib/domain/entities/Language';
import Organization from '../../lib/domain/entities/Organization';
import Role from '../../lib/domain/entities/Role';
import User from '../../lib/domain/entities/User';
import FeatureService from '../../lib/services/FeatureService';
import LanguageService from '../../lib/services/LanguageService';
import OrganizationService from '../../lib/services/OrganizationService';
import RoleService from '../../lib/services/RoleService';
import UserService from '../../lib/services/UserService';
import { useAuthState } from '../../page/authContext';
import { FormTitle, MediumForm, PanelBreak } from '../common/style';
import useConfiguration from '../config/useConfiguration';
import { useUserSchema } from './useUserSchema';

interface Props {
    roleService?: RoleService;
    organizationService?: OrganizationService;
    featureService?: FeatureService;
    languageService?: LanguageService;
    userService?: UserService;
    data?: User;
    openList(): void;
}

const UserForm: React.FC<Props> = ({
                                       roleService = defaultRoleService,
                                       organizationService = defaultOrganizationService,
                                       featureService = defaultFeatureService,
                                       languageService = defaultLanguageService,
                                       userService = defaultUserService,
                                       data,
                                       openList
                                   }) => {
    const { i18n } = useTranslation(['common', 'message', 'user']);
    const { fields, validations } = useUserSchema();
    const { updateConfiguration } = useConfiguration();
    const { configuration, updateUserSetting, isSameUserAsLoggedInUser } = useAuthState();

    const [features, setFeatures] = React.useState<Feature[]>([]);
    const [languageEnum, setLanguageEnum] = React.useState<Language[]>([]);
    const [organizations, setOrganizations] = React.useState<Organization[]>([]);
    const [roles, setRoles] = React.useState<Role[]>([]);
    const [passwordConfirmation, setPasswordConfirmation] = React.useState('');
    const [errorPasswordConfirmation, setErrorPasswordConfirmation] = React.useState('');
    const user: User = data || new User();

    const isEditMode = (): boolean => {
        return values[fields.id.name];
    };

    React.useEffect(() => {
        loadForm();
    }, []);

    const loadForm = async () => {
        setOrganizations(await organizationService.findAll());
        setRoles(await roleService.findAll());
        setFeatures(await featureService.findAll());
        setLanguageEnum((await languageService.findAll()));
    };

    const getInitialValues = (): any => {
        return {
            ...user.toPlainObj(),
            [fields.password.name]: '',
            [fields.organization.name]: user.organization.id,
            [fields.role.name]: user.role.id,
            [fields.language.name]: user.language ? user.language.id : '',
        };
    };

    const onSubmit = async (v: any) => {
        if (!validate()) {
            return Promise.reject();
        }
        await save(v.values);
        if (touched[fields.language.name]) {
            await updateConfiguration(v.values[fields.identifier.name]);
        }
        return;
    };

    const {
        values,
        touched,
        errors,
        handleChange,
        handleBlur,
        handleSubmit,
        handleMultipleChange
    } = useCustomForm({
        initialValues: getInitialValues(),
        onSubmit: async (v: any) => {
            await onSubmit(v);
        },
        validations,
        redirectAfterSave: openList
    });

    const validate = (): boolean => {
        const error = validatePasswordConfirmation(passwordConfirmation, values[fields.password.name]);
        if (error !== '') {
            setErrorPasswordConfirmation(error);
            return false;
        }
        return true;
    };

    const save = async (valuesToSave: any) => {
        let userToSave = prepareToSave(valuesToSave);

        if (userToSave.id) {
            await userService.update(userToSave);
        } else {
            userToSave = await userService.add(userToSave);
        }
        toast.success(i18n.t('message:success:save', { what: userToSave.identifier }));
        updateUserSetting('User', userToSave);
    };

    const prepareToSave = (valuesToSave: any): User => {
        const userToSave: User = new User();
        userToSave.applyData(valuesToSave);
        userToSave.organization = organizations.find(o => o.id.toString() === String(valuesToSave[fields.organization.name])) || new Organization();
        userToSave.role = roles.find(r => r.id.toString() === String(valuesToSave[fields.role.name])) || new Role();
        userToSave.language = languageEnum.find(r => r.id.toString() === String(valuesToSave[fields.language.name]));
        return userToSave;
    };

    const onCancelClick = () => {
        openList();
    };

    const handleRoleChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        const { value } = e.target;
        const role = roles.find((r: any) => r.id.toString() === value.toString()) || new Role();
        handleMultipleChange([
            { name: fields.role.name, value: e.target.value },
            { name: fields.features.name, value: role.defaultFeatures }
        ]);
    };

    const passwordChangeHandler = (e: React.ChangeEvent<HTMLInputElement>) => {
        handleChange(e);
        setErrorPasswordConfirmation(validatePasswordConfirmation(passwordConfirmation, e.target.value));
    };

    const passwordConfirmationChangeHandler = (e: React.ChangeEvent<HTMLInputElement>) => {
        e.preventDefault();
        setErrorPasswordConfirmation(validatePasswordConfirmation(e.target.value, values[fields.password.name]));
        setPasswordConfirmation(e.target.value);
    };

    const validatePasswordConfirmation = (value: string, password: string): string => {
        if (password && password !== value) {
            return i18n.t('message:validation:match', {what1: fields.passwordConfirmation.label, what2: fields.password.label});
        }
        return '';
    };

    const featureCheckboxChangeHandler = (e: ChangeEvent<HTMLInputElement>) => {
        const input = e.target.value;
        const listFeaturesChosen: Feature[] = values[fields.features.name];

        let listClone: Feature[] = [...listFeaturesChosen];

        if (isFeatureChecked(input)) {
            listClone = listFeaturesChosen.filter(o => o.id.toString() !== input.toString());
        } else {
            listClone.push(features.filter(o => o.id.toString() === input.toString())[0]);
        }
        handleMultipleChange([{ name: fields.features.name, value: listClone }]);
    };

    const isFeatureChecked = (id: string): boolean => {
        const listFeaturesChosen: Feature[] = values[fields.features.name];
        return !!listFeaturesChosen.find(o => o.id.toString() === id.toString());
    };

    const isFeatureDisable = (id: string): boolean => {
        const roleChosen: Role | undefined = roles.find(r => r.id.toString() === String(values[fields.role.name]));

        if (!roleChosen) {
            return true;
        }

        return !!roleChosen.nonEditableFeatures.find(f => f.id.toString() === id.toString());
    };

    const isRoleHigherOrEqualsThanLoggedInUser = (role: Role): boolean => {
        return !!configuration.user && configuration.user.role.level <= role.level;
    };

    return (
        <MediumForm onSubmit={handleSubmit}>
            {/*this is the solution to prevent browser to auto complete password and username fields*/}
            <input type="password" style={{'display': 'none'}} autoComplete='new-password'/>

            <FormTitle>{i18n.t('user:titlePlural')}</FormTitle>
            <PanelBreak/>

            <TextInput
                autoFocus={!isEditMode()}
                disabled={isEditMode()}
                minLength={fields.identifier.minSize}
                maxLength={fields.identifier.maxSize}
                label={fields.identifier.label}
                name={fields.identifier.name}
                value={values[fields.identifier.name]}
                isValid={!errors[fields.identifier.name]}
                validationErrorMessage={errors[fields.identifier.name]}
                onChange={handleChange}
                onBlur={handleBlur}
            />

            <br/>

            <TextInput
                autoFocus={isEditMode()}
                maxLength={fields.userName.maxSize}
                label={fields.userName.label}
                name={fields.userName.name}
                value={values[fields.userName.name]}
                isValid={!errors[fields.userName.name]}
                validationErrorMessage={errors[fields.userName.name]}
                onChange={handleChange}
                onBlur={handleBlur}
            />

            <br/>

            <TextInput
                label={fields.email.label}
                name={fields.email.name}
                value={values[fields.email.name]}
                isValid={!errors[fields.email.name]}
                validationErrorMessage={errors[fields.email.name]}
                onChange={handleChange}
                onBlur={handleBlur}
            />

            <br/>

            <TextInput
                type="password"
                minLength={fields.password.minSize}
                maxLength={fields.password.maxSize}
                label={fields.password.label}
                name={fields.password.name}
                value={values[fields.password.name]}
                isValid={!errors[fields.password.name]}
                validationErrorMessage={errors[fields.password.name]}
                onChange={passwordChangeHandler}
                onBlur={handleBlur}
            />

            <br/>

            <TextInput
                type="password"
                minLength={fields.password.minSize}
                maxLength={fields.password.maxSize}
                label={fields.passwordConfirmation.label}
                name={fields.passwordConfirmation.name}
                value={passwordConfirmation}
                isValid={!errorPasswordConfirmation}
                validationErrorMessage={errorPasswordConfirmation}
                onChange={passwordConfirmationChangeHandler}
                onBlur={handleBlur}
            />

            <br/>

            <Select
                label={fields.language.label}
                name={fields.language.name}
                emptySelectText="---"
                options={languageEnum.map((g: Language) => ({
                    label: g.name,
                    value: g.id
                }))}
                value={values[fields.language.name]}
                isValid={!errors[fields.language.name]}
                validationErrorMessage={errors[fields.language.name]}
                onChange={handleChange}
            />

            <br/>

            <Select label={fields.organization.label}
                    name={fields.organization.name}
                    value={values[fields.organization.name]}
                    emptySelectText="---"
                    options={organizations.map((o: Organization) => ({
                        label: o.name,
                        value: o.id
                    }))}
                    isValid={!errors[fields.organization.name]}
                    validationErrorMessage={errors[fields.organization.name]}
                    disabled={isSameUserAsLoggedInUser(user)}
                    onChange={handleChange}
            />

            <br/>

            <Select label={fields.role.label}
                    name={fields.role.name}
                    value={values[fields.role.name]}
                    emptySelectText="---"
                    options={roles.filter(isRoleHigherOrEqualsThanLoggedInUser).map((r: Role) => ({
                        label: i18n.t(BaseEnum.getValue(Role, r.id).name),
                        value: r.id
                    }))}
                    isValid={!errors[fields.role.name]}
                    validationErrorMessage={errors[fields.role.name]}
                    disabled={isSameUserAsLoggedInUser(user)}
                    onChange={handleRoleChange}
            />

            <br/>

            <label>{fields.features.label}</label>
            <br/>

            {features.map(f => (
                <>
                    <label style={{ fontSize: 'smaller', cursor: 'pointer' }}>
                        <input
                            style={{ cursor: 'pointer' }}
                            type={'checkbox'}
                            value={f.id}
                            onChange={featureCheckboxChangeHandler}
                            checked={isFeatureChecked(f.id)}
                            disabled={isFeatureDisable(f.id) || isSameUserAsLoggedInUser(user)}
                        />
                        {i18n.t(BaseEnum.getValue(Feature, f.id).name)}
                    </label>
                    <br/>
                </>
            ))}

            <br/>

            <CancelAndSaveButton onCancelClick={onCancelClick}/>

        </MediumForm>
    );
};

export default UserForm;
