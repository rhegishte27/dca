import { SplitWrapper } from 'equisoft-design-ui-elements';
import React from 'react';
import styled from 'styled-components';
import EditorArea from '../../components/editor/EditorArea';
import NavBar from '../../components/navigation/navbar/NavBar';
import { Panel } from '../../components/navigation/navbar/style';
import SidebarContent from '../../components/navigation/sidebar/SidebarContent';
import { SIDEBAR_MIN_SIZE, useSidebarContext } from '../../components/navigation/sidebar/SidebarProvider';

const EditorAreaStyle = styled.div`
    height: 100%;
    width: calc(100vw - 56px)
`;

const AppContent = () => {
    const {sidebarSize, setSidebarSize, openSidebar, closeSidebar} = useSidebarContext();
    const snap = SIDEBAR_MIN_SIZE; // %
    const sidebarDragged = (s: number[]) => {
        if (sidebarSize === 0) openSidebar('');
        if (s[0] < snap) closeSidebar();
        setSidebarSize(s[0]);
    };

    React.useEffect( () => {
        openSidebar('Explorer');
    }, []);

    return (
        <>
            <NavBar/>
            <EditorAreaStyle>
                <SplitWrapper
                    cursor={'col-resize'}
                    forceSizes={[sidebarSize, 100 - sidebarSize]}
                    snapPercent={snap}
                    onDragEnd={sidebarDragged}
                >
                    <Panel hidden={!sidebarSize}>
                        <SidebarContent/>
                    </Panel>
                    <EditorArea/>
                </SplitWrapper>
            </EditorAreaStyle>
        </>
    );
};

export default AppContent;