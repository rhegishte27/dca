import { TextAreaInput, TextInput } from 'equisoft-design-ui-elements';
import React from 'react';
import { useTranslation } from 'react-i18next';
import { toast } from 'react-toastify';
import useCustomForm from '../../components/form/Form';
import CancelAndSaveButton from '../../components/general/button/CancelAndSaveButtons';
import { defaultOrganizationService } from '../../lib/context';
import Organization from '../../lib/domain/entities/Organization';
import OrganizationService from '../../lib/services/OrganizationService';
import { useAuthState } from '../../page/authContext';
import { FormTitle, MediumForm, PanelBreak } from '../common/style';
import { getTextAreaRows } from '../common/util';
import { useOrganizationSchema } from './useOrganizationSchema';

interface Props {
    organizationService?: OrganizationService;
    data?: Organization;

    openList(): void;
}

const OrganizationForm: React.FC<Props> = ({organizationService = defaultOrganizationService, data, openList}) => {
    const {i18n} = useTranslation(['message', 'organization']);
    const { updateUserSetting } = useAuthState();
    const { fields, validations } = useOrganizationSchema();
    const initialValues = (data ? data : new Organization()).toPlainObj();

    const save = async (v: any) => {
        let organization: Organization = new Organization();
        organization.applyData(v.values);

        if (organization.id) {
            await organizationService.update(organization);
        } else {
            organization = await organizationService.add(organization);
        }
        toast.success(i18n.t('message:success:save', { what: organization.name }));
        updateUserSetting('Organization', organization);
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

    return (

        <MediumForm onSubmit={handleSubmit}>
            <FormTitle>{i18n.t('organization:titlePlural')}</FormTitle>
            <PanelBreak/>

            <TextInput
                autoFocus
                label={fields.name.label}
                name={fields.name.name}
                maxLength={fields.name.maxSize}
                isValid={!errors[fields.name.name]}
                validationErrorMessage={errors[fields.name.name]}
                value={values[fields.name.name]}
                onChange={handleChange}
                onBlur={handleBlur}
            />
            <br/>
            <TextAreaInput
                label={fields.description.label}
                name={fields.description.name}
                maxLength={fields.description.maxSize}
                isValid={!errors[fields.description.name]}
                validationErrorMessage={errors[fields.description.name]}
                value={values[fields.description.name]}
                rows={getTextAreaRows(values[fields.description.name])}
                onChange={handleChange}
                onBlur={handleBlur}
            />
            <br/>

            <CancelAndSaveButton onCancelClick={onCancelClick}/>

        </MediumForm>
    );
};

export default OrganizationForm;
