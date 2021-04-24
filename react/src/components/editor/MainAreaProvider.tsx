import React, { useMemo, useState } from 'react';
import { useConfirmationDialogContext } from '../general/dialog/confirmation/ConfirmationDialogProvider';

export class MainAreaData {
    type: MainAreaType = '';
    data: any;
}

export interface MainAreaContextProps {
    mainAreaData: MainAreaData;
    shouldAskBeforeLeave: boolean;
    refreshMainAreaListPage: boolean;

    setRefreshMainAreaListPage(): void;

    updateMainAreaListPage(updateMainAreaListPage: () => void): void;
    setShouldAskBeforeLeave(touched: boolean): void;

    openMainArea(type?: MainAreaType, dataForm?: any, actionsWhenUserClickOk?: () => Promise<void>): void;
}

export const MainAreaContext = React.createContext<MainAreaContextProps | undefined>(undefined);

export const useMainAreaContext = () => {
    const context = React.useContext(MainAreaContext);
    if (context === undefined) {
        throw new Error('useMainAreaContext must be used within an MainAreaProvider');
    }
    return context;
};

export type MainAreaType =
    | 'OrganizationList'
    | 'OrganizationForm'
    | 'UserList'
    | 'UserForm'
    | 'SystemList'
    | 'SystemForm'
    | 'ProjectList'
    | 'ProjectForm'
    | 'SettingForm'
    | 'LocationForm'
    | 'LocationList'
    | 'DataObjectContainerForm'
    | 'DataObjectCreateForm'
    | 'DataObjectImportForm'
    | 'DataObjectResultForm'
    | 'DataObjectSource'
    | '';

const MainAreaProvider = (props: any) => {
    const [mainAreaData, setMainAreaData] = useState<MainAreaData>(new MainAreaData());
    const [refreshMainAreaListPage, setRefreshMainAreaList] = useState<boolean>(false);
    const [shouldAskBeforeLeave, setShouldAskBeforeLeave] = useState<boolean>(false);

    const { openConfirmationDialogQuitEditing } = useConfirmationDialogContext();

    const openMainArea = React.useCallback((type: MainAreaType, data?: any, actionsWhenUserClickOk?: () => Promise<void>) => {
        if (shouldAskBeforeLeave) {
            openConfirmationDialogQuitEditing(changeMainArea.bind(changeMainArea, type, data, actionsWhenUserClickOk));
        } else {
            changeMainArea(type, data, actionsWhenUserClickOk);
        }
    }, [shouldAskBeforeLeave]);

    const changeMainArea = (type: MainAreaType, data?: any, actionsWhenUserClickOk?: () => Promise<void>) => {
        if (actionsWhenUserClickOk) {
            actionsWhenUserClickOk().then(() => {
                setShouldAskBeforeLeave(false);
                setMainAreaData({ type: type, data: data });
            });
        } else {
            setShouldAskBeforeLeave(false);
            setMainAreaData({ type: type, data: data });
        }

    };

    const isMainAreaListPage = (): boolean => {
        const type = mainAreaData.type;
        return type === 'OrganizationList' || type === 'UserList' || type === 'ProjectList' || type === 'SystemList' || type === 'LocationList';
    };

    const setRefreshMainAreaListPage = () => {
        if (isMainAreaListPage()) {
            setRefreshMainAreaList(true);
        }
    };

    const updateMainAreaListPage = (updateMainArea: () => void) => {
        if (isMainAreaListPage() && refreshMainAreaListPage) {
            updateMainArea();
            setRefreshMainAreaList(false);
        }
    };

    const context = useMemo(
        () => ({
            refreshMainAreaListPage,
            openMainArea,
            mainAreaData,
            setRefreshMainAreaListPage,
            setShouldAskBeforeLeave,
            updateMainAreaListPage
        }),
        [
            refreshMainAreaListPage,
            openMainArea,
            mainAreaData,
            setRefreshMainAreaListPage,
            setShouldAskBeforeLeave,
            updateMainAreaListPage
        ]
    );

    return <MainAreaContext.Provider value={context} {...props} />;
};

export default MainAreaProvider;