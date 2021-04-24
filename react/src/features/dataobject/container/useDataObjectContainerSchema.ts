import { useTranslation } from 'react-i18next';
import DataObject from '../../../lib/domain/entities/DataObject';
import DataObjectLocationType from '../../../lib/domain/entities/DataObjectLocationType';
import { useValidation } from '../../common/useValidation';
import { useDataObjectFormUtils } from '../common/useDataObjectFormUtils';

export const useDataObjectContainerSchema = () => {
    const { required } = useValidation();
    const { i18n } = useTranslation(['dataObject']);
    const { getOriginalFileName } = useDataObjectFormUtils();

    const fields = {
        dataObjectType: {
            label: i18n.t('dataObject:fileType'),
            name: 'fileType'
        },
        dataObjectLocationType: {
            label: i18n.t('dataObject:fileLocationType'),
            name: 'fileLocationType'
        },
        location: {
            label: i18n.t('dataObject:location'),
            name: 'location'
        },
        dataObjectFileList: {
            label: i18n.t('dataObject:file'),
            name: 'dataObjectList'
        }
    };

    const validateFileName = (value: DataObject[]): string => {
        const getFilesName = (): string => {
            let fileName = '';
            value.forEach(f => fileName = fileName + ', ' + getOriginalFileName(f));
            return fileName;
        };

        return required(fields.dataObjectFileList.label, getFilesName());
    };

    const validateDataObjectType = (value: string): string =>
        required(fields.dataObjectType.label, value);

    const validateLocation = (value: string, values: any): string => {
        if (String(values[fields.dataObjectLocationType.name]).toString() !== DataObjectLocationType.FTP.id) {
            return '';
        }
        return required(fields.location.label, value);
    };

    const validations = {
        [fields.dataObjectFileList.name]: validateFileName,
        [fields.dataObjectType.name]: validateDataObjectType,
        [fields.location.name]: validateLocation
    };

    return { fields, validations };
};