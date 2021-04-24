import React from 'react';
import { useTranslation } from 'react-i18next';
import BaseEnum from '../../../lib/domain/entities/BaseEnum';
import DataObject from '../../../lib/domain/entities/DataObject';
import DataObjectContainer from '../../../lib/domain/entities/DataObjectContainer';
import DataObjectFile from '../../../lib/domain/entities/DataObjectFile';
import DataObjectStatus from '../../../lib/domain/entities/DataObjectStatus';
import { sortList } from '../../common/listUtil';

export const useDataObjectFormUtils = () => {
    const { i18n } = useTranslation();

    const getContentToDisplay = (content: string): React.ReactNode => {
        const getLineColor = (line: string): string => {
            const lineTrim = line.trim();
            if (lineTrim.startsWith('ERROR:')) {
                return 'red';
            } else if (lineTrim.startsWith('WARNING:')) {
                return 'orange';
            } else if (lineTrim.startsWith('INFORM:')) {
                return 'green';
            }
            return '';
        };

        const getLineHtml = (line: string): React.ReactNode => {
            return (
                <>
                    <span style={{ color: getLineColor(line), fontWeight: getLineColor(line) ? 'bold' : undefined }}>
                        {line}
                    </span>
                    <br/>
                </>);
        };

        const lines: string[] = content ? content.split('\n') : [];

        return (
            <>
                {
                    lines.map((line: string) => (
                        getLineHtml(line)
                    ))
                }
            </>
        );
    };

    const getStatusDataObjectFile = (dataObjectFile: DataObjectFile, isDataObjectSaved: boolean): string => {
        return getStatus(dataObjectFile.status, isDataObjectSaved);
    };

    const getStatusDataObject = (dataObject: DataObject, isDataObjectSaved: boolean): string => {
        return getStatus(dataObject.status, isDataObjectSaved);
    };

    const getStatus = (status: DataObjectStatus | undefined, isDataObjectSaved: boolean): string => {
        if (status) {
            if (isDataObjectSaved) {
                return i18n.t(BaseEnum.getValue(DataObjectStatus, status.id).name);
            } else {
                if (status.id.toString() === DataObjectStatus.ERROR.id.toString()) {
                    return i18n.t('dataObject:message:fileNotMatchSelectedType');
                }

                return i18n.t('dataObject:notSelected');
            }
        }
        return '';
    };

    const getStatusColorDataObjectFile = (dataObjectFile: DataObjectFile, isDataObjectSaved: boolean): string => {
        return getStatusColor(dataObjectFile.status, isDataObjectSaved);
    };

    const getStatusColorDataObject = (dataObject: DataObject | undefined, isDataObjectSaved: boolean): string => {
        return getStatusColor(dataObject ? dataObject.status : undefined, isDataObjectSaved);
    };

    const getStatusColor = (status: DataObjectStatus | undefined, isDataObjectSaved: boolean): string => {
        if (status) {
            if (isDataObjectSaved) {
                if (status.id.toString() === DataObjectStatus.SUCCESS.id.toString()) {
                    return 'green';
                }
                if (status.id.toString() === DataObjectStatus.ERROR.id.toString()) {
                    return 'red';
                }
                if (status.id.toString() === DataObjectStatus.WARNING.id.toString()) {
                    return 'coral';
                }
            }
        }
        return 'grey';
    };

    const getIconStyle = (): any => {
        return {
            width: 30,
            height: 15,
            position: 'relative'
        };
    };

    const getFileName = (listDataObjectFile: DataObjectFile[]): string => {
        let fileName = '';

        listDataObjectFile.forEach((file, index) => {
            if (index !== 0) {
                fileName = fileName + ', ';
            }
            fileName = fileName + file.originalFileName;
        });
        return fileName;
    };

    const getDataObjectFile = (dataObject: DataObject): DataObjectFile | null => {
        return dataObject.dataObjectFiles ? dataObject.dataObjectFiles[0] : null;
    };

    const getOriginalFileName = (dataObject: DataObject): string => {
        const file: DataObjectFile | null = getDataObjectFile(dataObject);
        return file ? file.originalFileName : '';
    };

    const getStatusId = (dataObject: DataObject): string => {
        const file: DataObjectFile | null = getDataObjectFile(dataObject);
        return file ? (file.status ? file.status.id : '-1') : '-1';
    };

    const sortDataObjectFileListByFileName = (container: DataObjectContainer): DataObjectFile[] => {
        const dataFileList = [...container.dataObjectFileList];
        return sortList(dataFileList, (d: DataObjectFile) => d.originalFileName);
    };

    const sortDataObjectFileListByStatus = (container: DataObjectContainer): DataObjectFile[] => {
        const dataFileList = [...container.dataObjectFileList];
        return sortList(dataFileList, (d: DataObjectFile) => d.status ? d.status.id : '-1');
    };

    const sortListDataObjectByIdentifier = (container: DataObjectContainer): DataObjectFile[] => {
        const getDataObjectIdentifier = (dataObjectFile: DataObjectFile): string => {
            const dataObject: DataObject | undefined = container.dataObjectListImported.find(d => getOriginalFileName(d).toString() === dataObjectFile.originalFileName.toString());
            return dataObject ? dataObject.identifier : '';
        };

        const dataFileList = [...container.dataObjectFileList];

        return sortList(dataFileList, getDataObjectIdentifier);
    };

    return {
        getContentToDisplay,
        getStatusDataObjectFile,
        getStatusDataObject,
        getStatusColorDataObjectFile,
        getStatusColorDataObject,
        getIconStyle,
        getFileName,
        getOriginalFileName,
        getStatusId,
        sortDataObjectFileListByFileName,
        sortDataObjectFileListByStatus,
        sortListDataObjectByIdentifier
    };
};