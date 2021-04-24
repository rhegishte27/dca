import { useDialog } from 'equisoft-design-ui-elements';
import React, { useMemo, useState } from 'react';

export interface ConfirmDialogProps {
    show: boolean;
    type: DialogType;
    whatToDelete: string | undefined;

    onConfirm(): void;

    openConfirmationDialogQuitEditing(confirmClickHandler: () => void): void;

    openConfirmationDialogDelete(confirmClickHandler: () => void, whatToDelete?: string): void;

    closeDialog(): void;
}

export type DialogType =
    'Delete' |
    'LeaveChange'

const ConfirmationDialogContext = React.createContext<ConfirmDialogProps | undefined>(undefined);

export const useConfirmationDialogContext = () => {
    const context = React.useContext(ConfirmationDialogContext);
    if (context === undefined) {
        throw new Error('useConfirmationDialogContext must be used within an ConfirmationDialogProvider');
    }
    return context;
};

const ConfirmationDialogProvider = (props: any) => {
    const [show, toggle] = useDialog();
    const [type, setType] = useState<DialogType>('LeaveChange');
    const [whatToDelete, setWhatToDelete] = useState<string | undefined>();
    const [onConfirm, setOnConfirm] = useState<() => (() => void)>(() => (() => {}));

    const openConfirmationDialogQuitEditing = (confirmClickHandler: () => void) => {
        toggle();
        setOnConfirm(() => confirmClickHandler);
        setType('LeaveChange');
    };

    const openConfirmationDialogDelete = (confirmClickHandler: () => void, what?: string) => {
        toggle();
        setOnConfirm(() => confirmClickHandler);
        setWhatToDelete(what);
        setType('Delete');
    };

    const closeDialog = () => {
        toggle();
        setOnConfirm(() => (() => {}));
        setWhatToDelete(undefined);
    };

    const context = useMemo(
        () => ({
            show,
            type,
            whatToDelete,
            onConfirm,
            openConfirmationDialogQuitEditing,
            openConfirmationDialogDelete,
            closeDialog
        }),
        [
            show,
            type,
            whatToDelete,
            onConfirm,
            openConfirmationDialogQuitEditing,
            openConfirmationDialogDelete,
            closeDialog
        ]
    );

    return <ConfirmationDialogContext.Provider value={context} {...props} />;
};

export default ConfirmationDialogProvider;
