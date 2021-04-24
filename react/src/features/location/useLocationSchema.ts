import { useTranslation } from 'react-i18next';
import LocationType from '../../lib/domain/entities/LocationType';
import { useValidation } from '../common/useValidation';

export const useLocationSchema = () => {
    const { required, sizeLimit } = useValidation();
    const { i18n } = useTranslation('location');

    const fields = {
        id: {
            name: 'id'
        },
        identifier: {
            label: i18n.t('location:identifier'),
            name: 'identifier',
            minSize: 1,
            maxSize: 50
        },
        locationType: {
            label: i18n.t('location:locationType'),
            name: 'locationType'
        },
        path: {
            label: i18n.t('location:path'),
            name: 'path',
            minSize: 1,
            maxSize: 100,
            regExp: RegExp(/^(?:[a-z]:)?[\/\\]{0,2}(?:[.\/\\ ](?![.\/\\\n])|[^<>:"|?*.\/\\ \n])+$/gmi)
        },
        serverName: {
            label: i18n.t('location:serverName'),
            name: 'serverName',
            minSize: 1,
            maxSize: 100
        },
        userName: {
            label: i18n.t('location:userName'),
            name: 'userName',
            minSize: 1,
            maxSize: 50
        },
        password: {
            label: i18n.t('location:userPassword'),
            name: 'password',
            minSize: 1,
            maxSize: 50
        },
        platformType: {
            label: i18n.t('location:platformType'),
            name: 'platformType'
        }
    };

    const validateIdentifier = (value: string): string =>
        required(fields.identifier.label, value) ||
        sizeLimit(fields.identifier.label, value, fields.identifier.minSize, fields.identifier.maxSize);

    const validateLocationType = (value: string): string =>
        required(fields.locationType.label, value);

    const validatePath = (value: string, values: any): string =>
        required(fields.path.label, value) ||
        isLocationTypeFTP(values) ? '' : (fields.path.regExp.test(value) ? '' : i18n.t('message:validation:path', { what: fields.path.label })) ||
        sizeLimit(fields.path.label, value, fields.path.minSize, fields.path.maxSize);

    const isLocationTypeFTP = (values: any): boolean => {
        const locationType = values[fields.locationType.name];
        return locationType && String(locationType) === LocationType.FTP.id.toString();
    };

    const validateUserName = (value: string, values: any): string => {
        if (isLocationTypeFTP(values)) {
            return required(fields.userName.label, value) ||
                sizeLimit(fields.userName.label, value, fields.userName.minSize, fields.userName.maxSize);
        }
        return '';
    };

    const validateServerName = (value: string, values: any): string => {
        if (isLocationTypeFTP(values)) {
            return required(fields.serverName.label, value) ||
                sizeLimit(fields.serverName.label, value, fields.serverName.minSize, fields.serverName.maxSize);
        }
        return '';
    };

    const validatePassword = (value: string, values: any): string => {
        if (isLocationTypeFTP(values)) {
            return required(fields.password.label, value) ||
                sizeLimit(fields.password.label, value, fields.password.minSize, fields.password.maxSize);
        }
        return '';
    };

    const validatePlatformType = (value: string, values: any): string => {
        if (isLocationTypeFTP(values)) {
            return required(fields.platformType.label, value);
        }
        return '';
    };

    const validations = {
        [fields.identifier.name]: validateIdentifier,
        [fields.locationType.name]: validateLocationType,
        [fields.path.name]: validatePath,
        [fields.serverName.name]: validateServerName,
        [fields.userName.name]: validateUserName,
        [fields.password.name]: validatePassword,
        [fields.platformType.name]: validatePlatformType
    };

    return { fields, validations };
};