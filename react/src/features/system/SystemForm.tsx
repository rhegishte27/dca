import { TextAreaInput, TextInput } from 'equisoft-design-ui-elements';
import React from 'react';
import { useTranslation } from 'react-i18next';
import { toast } from 'react-toastify';
import useCustomForm from '../../components/form/Form';
import CancelAndSaveButton from '../../components/general/button/CancelAndSaveButtons';
import { defaultSystemService } from '../../lib/context';
import System from '../../lib/domain/entities/System';
import SystemService from '../../lib/services/SystemService';
import { useAuthState } from '../../page/authContext';
import { FormTitle, MediumForm, PanelBreak } from '../common/style';
import { getTextAreaRows } from '../common/util';
import { useSystemSchema } from './useSystemSchema';

interface Props {
    systemService?: SystemService;
    data?: System;
    openList(): void;
}

const SystemForm: React.FC<Props> = ({openList, systemService = defaultSystemService, data}) => {
    const { i18n } = useTranslation(['common', 'message', 'system']);
    const { updateUserSetting } = useAuthState();
    const { fields, validations } = useSystemSchema();
    const initialValues: any = (data ? data : new System()).toPlainObj();

    const save = async (v: any) => {
        let system: System = new System();
        system.applyData(v.values);

        if (system.id) {
            await systemService.update(system);
        } else {
            system = await systemService.add(system);
        }
        toast.success(i18n.t('message:success:save', { what: system.identifier }));
        updateUserSetting('System', system);
    };

    const {
        values,
        errors,
        handleChange,
        handleBlur,
        handleSubmit
    } = useCustomForm({
        initialValues,
        onSubmit: async (v: any) => {
            await save(v);
        },
        validations,
        redirectAfterSave: openList
    });

    const onCancelClick = () => {
        openList();
    };

    const isEditMode = (): boolean => {
        return values[fields.id.name];
    };

    return (
        <>
            <MediumForm onSubmit={handleSubmit}>
                <FormTitle>{i18n.t('system:titlePlural')}</FormTitle>
                <PanelBreak/>

                <TextInput
                    autoFocus={!isEditMode()}
                    disabled={isEditMode()}
                    label={fields.identifier.label}
                    name={fields.identifier.name}
                    value={values[fields.identifier.name]}
                    isValid={!errors[fields.identifier.name]}
                    validationErrorMessage={errors[fields.identifier.name]}
                    maxLength={fields.identifier.maxSize}
                    onChange={handleChange}
                    onBlur={handleBlur}
                />
                <br/>
                <TextAreaInput
                    autoFocus={isEditMode()}
                    label={fields.description.label}
                    name={fields.description.name}
                    value={values[fields.description.name]}
                    isValid={!errors[fields.description.name]}
                    validationErrorMessage={errors[fields.description.name]}
                    maxLength={fields.description.maxSize}
                    rows={getTextAreaRows(values[fields.description.name])}
                    onChange={handleChange}
                    onBlur={handleBlur}
                />
                <br/>

                <CancelAndSaveButton onCancelClick={onCancelClick}/>

            </MediumForm>
        </>
    );
};

export default SystemForm;
