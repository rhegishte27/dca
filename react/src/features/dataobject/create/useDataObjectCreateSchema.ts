import { useTranslation } from 'react-i18next';
import DataObject from '../../../lib/domain/entities/DataObject';

export const useDataObjectCreateSchema = () => {
    const { i18n } = useTranslation(['dataObject']);

    const fields = {
        selectedSystem: {
            label: i18n.t('dataObject:selectedSystem'),
            name: 'selectedSystem'
        },
        selectedFileType: {
            label: i18n.t('dataObject:selectedFileType'),
            name: 'selectedFileType'
        },
        selectedFiles: {
            label: i18n.t('dataObject:selectedFiles'),
            name: 'selectedFiles'
        },
        dataObjectFileList: {
            label: i18n.t('dataObject:file'),
            name: 'dataObjectList'
        },
        selectFilesMatchingExpression: {
            label: i18n.t('dataObject:selectFilesMatchingExpression'),
            name: 'selectFilesMatchingExpression'
        }
    };

    const validateDataObjectList = (value: DataObject[]): string => {
        return (value && value.length === 0) ? i18n.t('message:validation:required', { what: fields.dataObjectFileList.label }) : '';
    };

    const validations = {
        [fields.dataObjectFileList.name]: validateDataObjectList
    };

    return { fields, validations };
};