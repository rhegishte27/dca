import React from 'react';
import styled from 'styled-components';
import MainAreaProvider from '../../components/editor/MainAreaProvider';
import ConfirmationDialog from '../../components/general/dialog/confirmation/ConfirmationDialog';
import ConfirmationDialogProvider from '../../components/general/dialog/confirmation/ConfirmationDialogProvider';
import ErrorBoundary from '../../components/general/error/errorBoundary';
import Footer from '../../components/general/footer';
import Header from '../../components/general/header';
import SidebarProvider from '../../components/navigation/sidebar/SidebarProvider';
import useBeforeUnload from '../../features/common/useBeforeUnload';
import AppContent from './AppContent';

export const MainContent = styled.main`
    grid-area: main;
    display: flex;
    background-color: ${(props) => props.theme.colors.background.main};
`;

const Wrapper = styled.div`
    height: 100vh;
    display: grid;
    grid-template-areas: 'header' 'main' 'footer';
    grid-template-rows: 48px minmax(0, 1fr) 32px;
    grid-template-columns: 100vw;
`;

const App = () => {

    useBeforeUnload((event: BeforeUnloadEvent) => {
        if (event) {
            event.preventDefault();
        }
        return true;
    });

    return (

        <ErrorBoundary canLogout>
            <Wrapper>
                <ConfirmationDialogProvider>
                    <SidebarProvider>
                        <MainAreaProvider>
                            <Header/>
                            <MainContent>
                                <AppContent/>
                            </MainContent>
                            <Footer/>
                        </MainAreaProvider>
                    </SidebarProvider>

                    <ConfirmationDialog/>
                </ConfirmationDialogProvider>
            </Wrapper>
        </ErrorBoundary>
    );
};

export default App;
