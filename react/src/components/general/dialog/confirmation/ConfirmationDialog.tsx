import { Dialog } from 'equisoft-design-ui-elements';
import React from 'react';
import { useTranslation } from 'react-i18next';
import { useConfirmationDialogContext } from './ConfirmationDialogProvider';

const ConfirmationDialog: React.FC = ({}) => {
    const {i18n} = useTranslation('message');
    const { show, type, whatToDelete, closeDialog, onConfirm } = useConfirmationDialogContext();
    const handleConfirmClick = () => {
        onConfirm();
    };

    const getTitle = (): string => {
        switch (type) {
            case 'LeaveChange':
                return i18n.t('message:confirmation:quitEditTitle');
            case 'Delete':
                return whatToDelete ? i18n.t('message:confirmation:delete', { what: whatToDelete }) : i18n.t('message:confirmation:deleteMultiple');
        }
    };

    const getElement = (): React.ReactNode => {
        switch (type) {
            case 'LeaveChange':
                return <>{i18n.t('message:confirmation:quitEditDetail')}</>;
            case 'Delete':
                return <></>;
        }
    };

    const getDialogProps = () => {
        const baseProps = {
            show,
            onClose: closeDialog,
            title: getTitle(),
            confirmPanel: true,
            onConfirm: handleConfirmClick,
            element: getElement(),
            confirmMessage: i18n.t('common:button:yes'),
            cancelMessage: i18n.t('common:button:no')
        };
        return { ...baseProps };
    };

    return (
        <Dialog {...getDialogProps()} />
    );
};

export default ConfirmationDialog;