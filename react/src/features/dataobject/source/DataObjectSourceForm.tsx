import { Button, Dialog, Label, TextAreaInput, TextInput, useDialog } from 'equisoft-design-ui-elements';
import React, { useRef, useState } from 'react';
import { AlertCircle } from 'react-feather';
import { useTranslation } from 'react-i18next';
import { toast } from 'react-toastify';
import ReactTooltip from 'react-tooltip';
import useCustomForm from '../../../components/form/Form';
import useMenuItemActions from '../../../components/navigation/useMenuItemActions';
import { defaultDataObjectFileService, defaultDataObjectService, defaultSystemService } from '../../../lib/context';
import DataObject from '../../../lib/domain/entities/DataObject';
import DataObjectFile from '../../../lib/domain/entities/DataObjectFile';
import DataObjectStatus from '../../../lib/domain/entities/DataObjectStatus';
import System from '../../../lib/domain/entities/System';
import DataObjectFileService from '../../../lib/services/DataObjectFileService';
import DataObjectService from '../../../lib/services/DataObjectService';
import SystemService from '../../../lib/services/SystemService';
import { useAuthState } from '../../../page/authContext';
import { Actions, FormTitle, LargeForm, PanelBreak } from '../../common/style';
import { useDataObjectFormUtils } from '../common/useDataObjectFormUtils';
import { useDataObjectSourceSchema } from './useDataObjectSourceSchema';

interface Props {
    data: DataObject
    dataObjectService?: DataObjectService
    dataObjectResultService?: DataObjectFileService
    systemService?: SystemService

    openMainArea(): void;
}

const DataObjectSourceForm: React.FC<Props> = ({
                                                   openMainArea,
                                                   data,
                                                   dataObjectService = defaultDataObjectService,
                                                   dataObjectResultService = defaultDataObjectFileService,
                                                   systemService = defaultSystemService
                                               }) => {
    const { i18n } = useTranslation('dataObject');
    const { updateUserSetting } = useAuthState();
    const { fields, validations } = useDataObjectSourceSchema();
    const { deleteItem } = useMenuItemActions();
    const { getContentToDisplay, getIconStyle } = useDataObjectFormUtils();
    const [system, setSystem] = React.useState<System | undefined>();
    const [resultContent, setResultContent] = useState<string>('');
    const [dialogProps, setDialogProps] = useState({});
    const [show, toggle] = useDialog();
    const inputRef = useRef<HTMLInputElement>(null);

    React.useEffect(() => {
        const loadForm = async () => {
            setSystem(await systemService.findById(data.systemId));
        };

        loadForm();
    }, [data.systemId]);

    const onCancelClick = () => {
        openMainArea();
    };

    const getInitialValues = (): any => {
        return {
            [fields.source.name]: data.identifier
        };
    };

    const save = async () => {
        return Promise.resolve();
    };

    const {
        values,
        handleChange,
        handleBlur,
        handleSubmit
    } = useCustomForm({
        initialValues: getInitialValues(),
        onSubmit: async () => {
            await save();
        },
        validations,
        redirectAfterSave: openMainArea
    });

    const onSaveClick = async () => {
        const prepareToSave = (): DataObject => {
            const dataObject: DataObject = data;

            dataObject.identifier = values[fields.source.name];
            return dataObject;
        };

        const content: string = values[fields.source.name];

        if (!content.trim()) {
            deleteItem('DataObject', data, () => {
                inputRef.current?.click();
            });
        } else {
            const dataObjectToSave: DataObject = prepareToSave();

            await dataObjectService.update(dataObjectToSave);
            toast.success(i18n.t('message:success:save', { what: dataObjectToSave.identifier }));
            updateUserSetting('DataObject', dataObjectToSave);

            const result: DataObjectFile = await dataObjectResultService.findLatestFileByDataObjectId(dataObjectToSave.id);
            const dataObjectSaved: DataObject = await dataObjectService.findById(dataObjectToSave.id);
            if (dataObjectSaved.status && dataObjectSaved.status.id.toString() !== DataObjectStatus.ERROR.id.toString()) {
                inputRef.current?.click();
            } else {
                toggle();
                setResultContent(result.resultContent);
            }
        }
    };

    const getDialogProps = () => {
        const closeDialog = () => {
            setDialogProps({});
            setResultContent('');
            toggle();
        };

        const baseProps = {
            show,
            title: i18n.t('dataObject:result'),
            element:
                <>
                    <pre style={{ height: 400, overflow: 'auto', border: 'solid' }}>
                        {getContentToDisplay(resultContent)}
                    </pre>
                </>,
            onClose: closeDialog

        };
        return { ...baseProps, ...dialogProps };
    };

    const getDataTip = (): string => {
        return '<pre>' + i18n.t('dataObject:cobolLayoutSourceFile') + '</pre>';
    };

    return (
        <LargeForm onSubmit={handleSubmit}>
            <FormTitle>{i18n.t('dataObject:titlePlural')}</FormTitle>
            <PanelBreak/>

            <div>
                <TextInput
                    disabled
                    label={fields.identifier.label}
                    name={fields.identifier.name}
                    value={data.identifier}
                />

                <TextInput
                    disabled
                    label={fields.system.label}
                    name={fields.system.name}
                    value={system ? system.identifier : ''}
                />

                <br/>

                <Label>
                    {fields.source.label}
                    <AlertCircle data-tip={getDataTip()} data-for={'descriptionTip'} style={getIconStyle()}/>
                </Label>
                <TextAreaInput
                    style={{ height: '61vh' }}
                    autoFocus
                    name={fields.source.name}
                    value={values[fields.source.name]}
                    onChange={handleChange}
                    onBlur={handleBlur}
                />
                <br/>

                <Actions>
                    <Button type="button" buttonType='primary' onClick={onSaveClick}>
                        {i18n.t('common:button:save')}
                    </Button>
                    <Button type="button" buttonType='secondary' onClick={onCancelClick}>
                        {i18n.t('common:button:cancel')}
                    </Button>
                    <input hidden type="submit" ref={inputRef}/>
                </Actions>
            </div>
            <Dialog {...getDialogProps()} />
            <div style={{ textAlign: 'left' }}>
                <ReactTooltip id="descriptionTip" place="right" html type={'info'}/>
            </div>
        </LargeForm>
    );
};

export default DataObjectSourceForm;