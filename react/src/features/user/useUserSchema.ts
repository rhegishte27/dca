import {useTranslation} from 'react-i18next';
import {useValidation} from '../common/useValidation';

export const useUserSchema = () => {
    const { required, sizeLimit } = useValidation();
    const { i18n } = useTranslation(['message', 'user']);

    const fields = {
        id: {
            name: 'id'
        },
        identifier: {
            label: i18n.t('user:identifier'),
            name: 'identifier',
            minSize: 6,
            maxSize: 8,
            regExp: RegExp(/^[a-zA-Z][a-zA-Z0-9-]*$/)
        },
        userName: {
            label: i18n.t('user:userName'),
            name: 'name',
            minSize: 1,
            maxSize: 32
        },
        password: {
            label: i18n.t('user:password'),
            name: 'password',
            minSize: 1,
            maxSize: 32,
            regEx: RegExp('^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=*()"\',./])(?=\\S+$).{8,}$')
        },
        passwordConfirmation: {
            label: i18n.t('user:passwordConfirmation'),
            name: 'passwordConfirmation'
        },
        organization: {
            label: i18n.t('user:organization'),
            name: 'organization',
        },
        role: {
            label: i18n.t('user:role'),
            name: 'role',
        },
        features: {
            label: i18n.t('user:features'),
            name: 'features',
        },
        email: {
            label: i18n.t('user:email'),
            name: 'emailAddress',
            regEx: RegExp(/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/)
        },
        language: {
            label: i18n.t('user:language'),
            name: 'language'
        }
    };

    const validateIdentifier = (value: string): string =>
        required(fields.identifier.label, value) ||
        (fields.identifier.regExp.test(value) ? '' : i18n.t('message:validation:pattern', {what: fields.identifier.label})) ||
        sizeLimit(fields.identifier.label, value, fields.identifier.minSize, fields.identifier.maxSize);

    const validateUserName = (value: string): string =>
        required(fields.userName.label, value) ||
        sizeLimit(fields.userName.label, value, fields.userName.minSize, fields.userName.maxSize);

    const validatePassword = (value: string, values: any): string => {
        if (!values[fields.id.name] || value) {
            return required(fields.password.label, value) ||
                (fields.password.regEx.test(value) ? '' : i18n.t('user:message:passwordFormat')) ||
                sizeLimit(fields.password.label, value, fields.password.minSize, fields.password.maxSize);
        }
        return '';
    };

    const validateOrganization = (value: string): string =>
        required(fields.organization.label, value);

    const validateRole = (value: string): string =>
        required(fields.role.label, value);

    const validateEmail = (value: string): string =>
        fields.email.regEx.test(value) ? '' : i18n.t('message:validation:invalid', {what: fields.email.label});

    const validations: { [key: string]: any } = {
        [fields.identifier.name]: validateIdentifier,
        [fields.organization.name]: validateOrganization,
        [fields.role.name]: validateRole,
        [fields.userName.name]: validateUserName,
        [fields.password.name]: validatePassword,
        [fields.email.name]: validateEmail,
    };

    return { fields, validations };
};
