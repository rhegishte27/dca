import React from 'react';
import 'react-accessible-accordion/dist/fancy-example.css';
import 'react-contexify/dist/ReactContexify.min.css';
import { BrowserRouter as Router, Redirect, Route, RouteProps } from 'react-router-dom';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import 'reflect-metadata';
import AuthProvider, { isAuthenticated, useAuthState } from './authContext';
import './i18n';

const App = () => {
    toast.configure({ autoClose: 10000, draggable: false, closeOnClick: false, position: toast.POSITION.BOTTOM_RIGHT });

    return (
        <React.Suspense fallback={<div>Loading ...</div>}>
            <AuthProvider>
                <AppRouter />
            </AuthProvider>
        </React.Suspense>
    );
};

const Login = React.lazy(() => import('./login/login'));
const MainApp = React.lazy(() => import('./app/app'));

const AppRouter = () => {
    const { getContextPath } = useAuthState();
    const shouldShowLogin = isAuthenticated() === 'NO';

    return (
        <Router basename={`${getContextPath()}`}>
            <Route exact path="/" render={() => <Redirect to="/login" />} />
            <RouteWithRedirect condition={shouldShowLogin} redirectPath="/app" path="/login">
                <Login />
            </RouteWithRedirect>
            <RouteWithRedirect condition={!shouldShowLogin} redirectPath="/login" path="/app">
                <MainApp />
            </RouteWithRedirect>
        </Router>
    );
};

interface WithRedirectProps {
    condition: boolean;
    redirectPath: string;
}

const RouteWithRedirect = ({ condition, redirectPath, children, ...rest }: RouteProps & WithRedirectProps) => {
    return (
        <Route
            {...rest}
            render={(props) =>
                condition ? (
                    children
                ) : (
                    <Redirect
                        to={{
                            pathname: redirectPath,
                            state: { from: props.location },
                        }}
                    />
                )
            }
        />
    );
};

export default App;
