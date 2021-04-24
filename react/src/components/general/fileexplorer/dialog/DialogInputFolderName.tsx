import { Button, Dialog, TextInput } from 'equisoft-design-ui-elements';
import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Buttons } from '../style';

interface DialogInputFolderNameProps {
    show: boolean,

    toggle(): void;

    isFolderNameDuplicate(folderName: string): boolean,
    onConfirm(folderName: string): void;
}

const DialogInputFolderName: React.FC<DialogInputFolderNameProps> = ({show, isFolderNameDuplicate, toggle, onConfirm}) => {
    const {i18n} = useTranslation(['directory', 'common', 'message']);
    const [dialogProps, setDialogProps] = useState({});
    const [name, setName] = useState<string>('');
    const [isValid, setValid] = useState<boolean>(true);
    const [validationMessage, setValidationMessage] = useState<string>('');

    React.useEffect(() => {
        setName('');
        setValidationMessage('');
        validateFolderName('');
    }, [show]);

    const getDialogProps = () => {
        const baseProps = {
            show,
            onClose: closeDialog,
            title: i18n.t('directory:message:enterFolderName'),
            element:
                <>
                    <TextInput
                        label={i18n.t('directory:folderName')}
                        required
                        value={name}
                        onChange={handleOnChange}
                        isValid={isValid}
                        validationErrorMessage={validationMessage}
                    />

                    <br/>

                    <Buttons>
                        <Button
                            buttonType='secondary'
                            onClick={closeDialog}
                        >
                            {i18n.t('common:button:no')}
                        </Button>

                        <Button
                            disabled={!isValid}
                            buttonType='primary'
                            onClick={handleConfirmClick}
                        >
                            {i18n.t('common:button:yes')}
                        </Button>
                    </Buttons>
                </>,
        };
        return {...baseProps, ...dialogProps};
    };

    const closeDialog = () => {
        setDialogProps({});
        toggle();
    };

    const validateFolderName = (folderName: string): void => {
        let messageValidation = '';

        if (!folderName) {
            messageValidation = i18n.t('message:validation:required', {what: i18n.t('directory:folderName')});
        }

        if (isFolderNameDuplicate(folderName)) {
            messageValidation = i18n.t('directory:validation:folderNameDuplicated');
        }

        setValid(!messageValidation);
        setValidationMessage(messageValidation);
    };

    const handleOnChange = (event: React.ChangeEvent<HTMLInputElement>): void => {
        event.preventDefault();

        const folderName = event.target.value;
        validateFolderName(folderName);
        setName(folderName);
    };

    const handleConfirmClick = (): void => {
        onConfirm(name);
        closeDialog();
    };

    return (
        <Dialog {...getDialogProps()} />
    );
};

export default DialogInputFolderName;