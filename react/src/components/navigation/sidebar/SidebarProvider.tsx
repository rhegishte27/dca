import React, { useMemo, useState } from 'react';

class SidebarData {
    type: SidebarType = '';
    data: any;
}

interface SidebarContextProps {
    sidebarSize: number;
    refreshExplorerPanel: boolean;
    sidebarData: SidebarData;

    openSidebar(type: SidebarType, data?: any): void;

    closeSidebar(): void;

    setSidebarSize(size: number): void;

    setRefreshExplorerPanel(): void;

    updateExplorerPanel(updatePanel: () => void): void
}

export const SIDEBAR_MIN_SIZE = 15;

const SidebarContext = React.createContext<SidebarContextProps | undefined>(undefined);

export const useSidebarContext = () => {
    const context = React.useContext(SidebarContext);
    if (context === undefined) {
        throw new Error('useSidebarContext must be used within an SidebarProvider');
    }
    return context;
};

type SidebarType =
    | 'Explorer'
    | '';

const SidebarProvider = (props: any) => {
    const [sidebarSize, setSidebarSize] = useState(20);
    const [sidebarIsOpen, setSidebarIsOpen] = useState(false);
    const [refreshExplorerPanel, setRefreshExplorer] = useState(false);
    const [sidebarData, setSidebarData] = useState<SidebarData>(new SidebarData());

    const openSidebar = (type: SidebarType, data?: any) => {
        if (sidebarIsOpen && type === sidebarData.type) {
            closeSidebar();
            return;
        }

        if (sidebarSize < SIDEBAR_MIN_SIZE) {
            setSidebarSize(SIDEBAR_MIN_SIZE);
        }

        setSidebarData({type: type, data: data});
        setSidebarIsOpen(true);
    };

    const closeSidebar = () => {
        setSidebarData({type: '', data: null});
        setSidebarIsOpen(false);
    };

    const setRefreshExplorerPanel = () => {
        if (sidebarData.type === 'Explorer') {
            setRefreshExplorer(true);
        }
    };

    const updateExplorerPanel = (updatePanel: () => void) => {
        if (sidebarData.type === 'Explorer' && refreshExplorerPanel) {
            updatePanel();
            setRefreshExplorer(false);
        }
    };

    const context = useMemo(
        () => ({
            sidebarData,
            openSidebar,
            closeSidebar,
            sidebarSize: sidebarIsOpen ? sidebarSize : 0,
            setSidebarSize,
            refreshExplorerPanel,
            setRefreshExplorerPanel,
            updateExplorerPanel
        }),
        [
            sidebarData,
            sidebarSize,
            sidebarIsOpen,
            setSidebarSize,
            refreshExplorerPanel,
            openSidebar,
            closeSidebar,
            setSidebarData,
            setRefreshExplorerPanel,
            updateExplorerPanel
        ]
    );

    return <SidebarContext.Provider value={context} {...props} />;
};

export default SidebarProvider;
