import { Loading } from 'equisoft-design-ui-elements';
import React from 'react';
import ExplorerPanel from '../../../features/explorer/ExplorerPanel';
import ErrorBoundary from '../../general/error/errorBoundary';
import { PanelContent } from '../navbar/style';
import { useSidebarContext } from './SidebarProvider';

const SidebarContent = () => {
    const {sidebarData} = useSidebarContext();

    return (
        <PanelContent>
            {(() => {
                switch (sidebarData.type) {
                    case 'Explorer':
                        return <ExplorerPanel/>;
                    case '':
                        return <></>;
                }
            })()}
        </PanelContent>
    );

};

export default (props: any) => (
    <React.Suspense fallback={<Loading/>}>
        <ErrorBoundary>
            <SidebarContent {...props} />
        </ErrorBoundary>
    </React.Suspense>
);