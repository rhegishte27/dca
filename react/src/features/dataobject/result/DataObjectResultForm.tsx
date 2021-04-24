import { Button, Dialog, TextInput, useDialog } from 'equisoft-design-ui-elements';
import React, { useState } from 'react';
import { ChevronsDown, ChevronsUp } from 'react-feather';
import { useTranslation } from 'react-i18next';
import { useMainAreaContext } from '../../../components/editor/MainAreaProvider';
import { defaultDataObjectFileService, defaultDataObjectService } from '../../../lib/context';
import BaseEnum from '../../../lib/domain/entities/BaseEnum';
import DataObject from '../../../lib/domain/entities/DataObject';
import DataObjectContainer from '../../../lib/domain/entities/DataObjectContainer';
import DataObjectFile from '../../../lib/domain/entities/DataObjectFile';
import DataObjectStatus from '../../../lib/domain/entities/DataObjectStatus';
import DataObjectType from '../../../lib/domain/entities/DataObjectType';
import DataObjectFileService from '../../../lib/services/DataObjectFileService';
import DataObjectService from '../../../lib/services/DataObjectService';
import { Actions, DataObjectTableResult, FormTitle, MediumForm, PanelBreak } from '../../common/style';
import { useDataObjectFormUtils } from '../common/useDataObjectFormUtils';

interface Props {
    data: DataObjectContainer;
    dataObjectService?: DataObjectService;
    dataObjectFileService?: DataObjectFileService;

    onCancelClick(): void;
}

const DataObjectResultForm: React.FC<Props> = ({
                                                   data,
                                                   dataObjectService = defaultDataObjectService,
                                                   dataObjectFileService = defaultDataObjectFileService,
                                                   onCancelClick
                                               }) => {
    const { i18n } = useTranslation();
    const [listDataObjectSorted, setListDataObjectSorted] = useState<DataObjectFile[]>([]);
    const [isSortedByFileNameAsc, setIsSortedByFileNameAsc] = useState<boolean>(false);
    const [isSortedByIdentifierAsc, setIsSortedByIdentifierAsc] = useState<boolean>(false);
    const [isSortedByStatusAsc, setIsSortedByStatusAsc] = useState<boolean>(false);
    const { openMainArea, setShouldAskBeforeLeave } = useMainAreaContext();
    const {
        getContentToDisplay, getStatusDataObject, getStatusColorDataObject, getIconStyle, getFileName,
        getOriginalFileName, sortDataObjectFileListByFileName, sortListDataObjectByIdentifier, sortDataObjectFileListByStatus
    } = useDataObjectFormUtils();
    const [fileToRead, setFileToRead] = useState<DataObjectFile | undefined>();
    const [dialogProps, setDialogProps] = useState({});
    const [show, toggle] = useDialog();

    React.useEffect(() => {
        setShouldAskBeforeLeave(true);
        setListDataObjectSorted(sortDataObjectFileListByFileName(data));
        setIsSortedByFileNameAsc(true);
    }, []);

    const getDataObjectSaved = (dataObjectFile: DataObjectFile): DataObject | undefined => {
        return data.dataObjectListImported.find(d => getOriginalFileName(d).toString() === dataObjectFile.originalFileName.toString());
    };

    const getDataObjectIdentifierAfterSave = (dataObjectFile: DataObjectFile): string => {
        const dataObjectSaved = getDataObjectSaved(dataObjectFile);
        if (dataObjectSaved) {
            return dataObjectSaved.identifier;
        }
        return '';
    };

    const getDialogProps = () => {
        const closeDialog = () => {
            setDialogProps({});
            setFileToRead(undefined);
            toggle();
        };

        const baseProps = {
            show,
            title: fileToRead ? fileToRead.originalFileName : '',
            element:
                <>
                    <pre style={{ height: 400, overflow: 'auto', border: 'solid' }}>
                        {getContentToDisplay(fileToRead ? fileToRead.resultContent : '')}
                    </pre>
                </>,
            onClose: closeDialog

        };
        return { ...baseProps, ...dialogProps };
    };

    const getStatusNode = (item: DataObjectFile): React.ReactNode => {
        const isStatusWarningOrError = (dataObject: DataObject): boolean => {
            return !!dataObject.status &&
                (dataObject.status.id.toString() === DataObjectStatus.ERROR.id.toString() || dataObject.status.id.toString() === DataObjectStatus.WARNING.id.toString());
        };

        const dataObjectSaved = getDataObjectSaved(item);
        const statusLabel: React.ReactNode = getStatusDataObject(dataObjectSaved ? dataObjectSaved : new DataObject(), !!dataObjectSaved);

        if (dataObjectSaved && isStatusWarningOrError(dataObjectSaved)) {
            return (
                <div style={{ cursor: 'pointer', textDecoration: 'underline' }}
                     onClick={async e => {
                         e.preventDefault();
                         toggle();
                         const dataObject: DataObject | undefined = getDataObjectSaved(item);
                         const dataObjectFile: DataObjectFile = await dataObjectFileService.findLatestFileByDataObjectId(dataObject ? dataObject.id : '-1');
                         setFileToRead(dataObjectFile);
                         return false;
                     }}>
                    {statusLabel}
                </div>
            );
        }

        return statusLabel;
    };

    return (

        <MediumForm>
            <FormTitle>{i18n.t('dataObject:titlePlural')}</FormTitle>
            <PanelBreak/>

            <TextInput
                disabled
                label={i18n.t('dataObject:selectedSystem')}
                value={data.system.identifier}
            />

            <TextInput
                disabled
                label={i18n.t('dataObject:selectedFileType')}
                value={String(i18n.t(BaseEnum.getValue(DataObjectType, data.type ? data.type.id : '').name))}
            />

            <TextInput
                disabled
                label={i18n.t('dataObject:selectedFiles')}
                value={getFileName(data.dataObjectFileList)}
            />

            <br/>

            {listDataObjectSorted.length > 0 && (
                <>
                    <DataObjectTableResult>
                        <thead>
                        <tr>
                            <th>
                                <label style={{ cursor: 'pointer' }} onClick={() => {
                                    const newList: DataObjectFile[] = [];
                                    Array.prototype.push.apply(newList, isSortedByFileNameAsc ? sortDataObjectFileListByFileName(data).reverse() : sortDataObjectFileListByFileName(data));
                                    setListDataObjectSorted(newList);
                                    setIsSortedByFileNameAsc(!isSortedByFileNameAsc);
                                }}>
                                    {i18n.t('dataObject:file')}
                                    {isSortedByFileNameAsc ? <ChevronsDown style={getIconStyle()}/> : <ChevronsUp style={getIconStyle()}/>}
                                </label>
                            </th>
                            <th>
                                <label style={{ cursor: 'pointer' }} onClick={() => {
                                    const newList: DataObjectFile[] = [];
                                    Array.prototype.push.apply(newList, isSortedByIdentifierAsc ? sortListDataObjectByIdentifier(data).reverse() : sortListDataObjectByIdentifier(data));
                                    setListDataObjectSorted(newList);
                                    setIsSortedByIdentifierAsc(!isSortedByIdentifierAsc);
                                }}>
                                    {i18n.t('dataObject:identifier')}
                                    {isSortedByIdentifierAsc ? <ChevronsDown style={getIconStyle()}/> : <ChevronsUp style={getIconStyle()}/>}
                                </label>
                            </th>
                            <th>
                                <label style={{ cursor: 'pointer' }} onClick={() => {
                                    const newList: DataObjectFile[] = [];
                                    Array.prototype.push.apply(newList, isSortedByStatusAsc ? sortDataObjectFileListByStatus(data).reverse() : sortDataObjectFileListByStatus(data));
                                    setListDataObjectSorted(newList);
                                    setIsSortedByStatusAsc(!isSortedByStatusAsc);
                                }}>
                                    {i18n.t('dataObject:status')}
                                    {isSortedByStatusAsc ? <ChevronsDown style={getIconStyle()}/> : <ChevronsUp style={getIconStyle()}/>}
                                </label>
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        {listDataObjectSorted.map((item) => (
                            <tr key={item.originalFileName}>
                                <td>{item.originalFileName}</td>

                                <td>
                                    <a href={'#'}
                                       onClick={e => {
                                           e.preventDefault();
                                           toggle();
                                           setFileToRead(item);
                                           return false;
                                       }}>
                                        {getDataObjectIdentifierAfterSave(item)}
                                    </a>
                                </td>
                                <td style={{ color: getStatusColorDataObject(getDataObjectSaved(item), !!getDataObjectSaved(item)) }}>
                                    {getStatusNode(item)}
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </DataObjectTableResult>
                </>
            )}

            <br/>

            <Actions>
                <Button type="button" buttonType='secondary' onClick={() => {

                    openMainArea('DataObjectContainerForm', data, async () => {
                        data.dataObjectFileListChosenInCreateForm = [];
                        data.dataObjectListChosenInImportForm = [];
                        data.dataObjectListImported = [];
                    });
                }}>
                    {i18n.t('dataObject:selectFiles')}
                </Button>
                <Button type="button" buttonType='secondary' onClick={() => {
                    openMainArea('DataObjectCreateForm', data, async () => {
                        data.dataObjectListChosenInImportForm = [];
                        data.dataObjectListImported = [];
                    });
                }}>
                    {i18n.t('dataObject:createDataObjects')}
                </Button>
                <Button type="button" buttonType='secondary' onClick={() => {
                    openMainArea('DataObjectImportForm', data, async () => {
                        data.dataObjectListChosenInImportForm = await dataObjectService.create(data);
                        data.dataObjectListImported = [];
                    });
                }}>
                    {i18n.t('dataObject:selectImport')}
                </Button>
                <Button type="button" buttonType='secondary' onClick={onCancelClick}>
                    {i18n.t('common:button:cancel')}
                </Button>
            </Actions>
            <Dialog {...getDialogProps()} />
        </MediumForm>
    );
};

export default DataObjectResultForm;
