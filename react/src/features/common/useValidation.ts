import {useTranslation} from 'react-i18next';

export const useValidation = () => {
    const { i18n } = useTranslation(['message']);

    const required = (fieldName: string, fieldValue: string): string =>
        (fieldValue === undefined || fieldValue === null || fieldValue === '') ? i18n.t('message:validation:required', {what : fieldName}) : '';

    const isEmpty = (fieldValue: string): boolean =>
        (fieldValue === undefined || fieldValue === null || fieldValue === '');

    const sizeLimit = (fieldName: string, fieldValue: string, minSize: number, maxSize: number): string =>
        (fieldValue.length > maxSize || fieldValue.length < minSize) ? i18n.t('message:validation:sizeLimit', {what : fieldName, min: minSize, max: maxSize}) : '';

    const isNumber = (fieldName: string, fieldValue: any): string => {
        return Number.isNaN(fieldValue) ? i18n.t('message:validation:isNumber', {what : fieldName}) : '';
    };

    const numberRange = (fieldName: string, fieldValue: any, min: number, max: number): string => {
        return isNumber(fieldName, fieldValue)
            || ((+fieldValue < min || +fieldValue > max) ? i18n.t('message:validation:numberRange', {
                what: fieldName,
                min: min,
                max: max
            }) : '');
    };

    const greaterThanZero = (fieldName: string, fieldValue: any): string => {
        return isNumber(fieldName, fieldValue) || (+fieldValue <= 0 ? i18n.t('message:validation:greaterThanZero', {what : fieldName}) : '');
    };

    const isGreaterThanZero = (fieldValue: any): boolean => {
        return Number.isNaN(fieldValue) || +fieldValue >= 0;
    };

    return {required, sizeLimit, isNumber, greaterThanZero, numberRange, isEmpty, isGreaterThanZero};
};
