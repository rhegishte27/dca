import { DesignThemeProvider } from 'equisoft-design-ui-elements';
import React, { useCallback, useMemo, useState } from 'react';
import { EntityType } from '../features/common/EntityType';
import useConfiguration from '../features/config/useConfiguration';
import { defaultAuthService, defaultInteropService, defaultUserSettingService } from '../lib/context';
import Configuration from '../lib/domain/entities/Configuration';
import User from '../lib/domain/entities/User';
import UserSetting from '../lib/domain/entities/UserSetting';
import AuthService from '../lib/services/authService';
import InteropService from '../lib/services/interopService';
import UserSettingService from '../lib/services/UserSettingService';

const LOCAL_STORAGE_KEY = 'tabContext';

export interface AuthContextProps {
    configuration: Configuration;
    isAuthenticated: AuthStatus;

    login(identifier: string, password: string): Promise<Configuration>;
    logout(): Promise<void>;
    getContextPath(): string;

    isSameUserAsLoggedInUser(user: User | undefined): boolean;

    isLoggedInUserHasHigherOrEqualsRole(user: User | undefined): boolean;

    updateUserSetting(entityType: EntityType, entity?: any): void;

    setConfiguration(configuration: Configuration): void;
}

export const AuthContext = React.createContext<AuthContextProps>({
    configuration: new Configuration(),

    isAuthenticated: 'NO',
    login: () => Promise.resolve(new Configuration()),
    logout: () => Promise.resolve(),
    getContextPath: () => '',
    isSameUserAsLoggedInUser: () => false,
    isLoggedInUserHasHigherOrEqualsRole: () => false,
    updateUserSetting: () => {},
    setConfiguration: () => {}
});

export const useAuthState = () => {
    const context = React.useContext(AuthContext);
    if (context === undefined) {
        throw new Error('useAuthState must be used within an AuthProvider');
    }
    return context;
};

export const isAuthenticated = () => {
    return useAuthState().isAuthenticated;
};

interface AuthProviderProps {
    authService: AuthService;
    userSettingService: UserSettingService;
    interopService: InteropService;
}

export type AuthStatus = 'YES' | 'NO' | 'LOST';

const AuthProvider = ({ authService: authService, userSettingService: userSettingService, interopService, ...props }: AuthProviderProps) => {
    const [configuration, setConfiguration] = useState<Configuration>(new Configuration());
    const [isAuth, setIsAuth] = useState<AuthStatus>('NO');
    const {updateConfiguration} = useConfiguration();

    const handleDisconnect = useCallback(() => {
        setIsAuth('LOST');
    }, [setIsAuth]);

    authService.setDisconnectHandler(handleDisconnect);

    const setAuthData = (config: Configuration): void => {
        setConfiguration(config);

        if (config.user && config.user.id) {
            setIsAuth('YES');
        }
    };

    const login = async (identifier: string, password: string): Promise<Configuration> => {
        return authService.login(identifier, password).then(async (d) => {
            setAuthData(d);
            await updateConfiguration();
            setIsAuth('YES');
            localStorage.removeItem(LOCAL_STORAGE_KEY);
            return d;
        });
    };

    const logout = async () => {
        return authService.logout().then(async (d) => {
            setIsAuth('NO');
            configuration.user = undefined;
            localStorage.removeItem(LOCAL_STORAGE_KEY);
            return d;
        });
    };

    const isLoggedInUserHasHigherOrEqualsRole = (user: User | undefined): boolean => {
        return !!configuration.user && !!user && Number(configuration.user.role.level) <= Number(user.role.level);
    };

    const isSameUserAsLoggedInUser = (user: User | undefined): boolean => {
        return !!configuration.user && !!user && configuration.user.id.toString() === user.id.toString();
    };

    const updateUserSetting = (entityType: EntityType, entity?: any) => {
        const getNewUserSetting = (config: Configuration): UserSetting => {
            const userSetting: UserSetting = new UserSetting();
            userSetting.id = config.user ? config.user.id : '';
            userSetting.user = config.user ? config.user : new User();
            return userSetting;
        };

        const newUserSetting: UserSetting = {
            ...(configuration.userSetting ? configuration.userSetting : getNewUserSetting(configuration))
        };

        switch (entityType) {
            case 'Project':
                newUserSetting.defaultProject = entity;
                break;
            case 'Organization':
                newUserSetting.defaultOrganization = entity;
                break;
            case 'User':
                newUserSetting.defaultUser = entity;
                break;
            case 'System':
                newUserSetting.defaultSystem = entity;
                break;
            case 'Location':
                newUserSetting.defaultLocation = entity;
                break;
            case 'DataObject':
                newUserSetting.defaultDataObject = entity;
                break;
            case '':
                break;
        }

        userSettingService.save(newUserSetting).then((u) => {
            const newConfiguration: Configuration = Object.create(configuration);
            newConfiguration.userSetting = u;
            setConfiguration(newConfiguration);
        });
    };

    const getContextPath = () => interopService.getContextPath();

    const context: AuthContextProps = useMemo(
        () => ({
            configuration: configuration,
            login,
            logout,
            isAuthenticated: isAuth,
            getContextPath,
            isLoggedInUserHasHigherOrEqualsRole,
            isSameUserAsLoggedInUser,
            updateUserSetting,
            setConfiguration
        }),
        [
            configuration,
            login,
            logout,
            getContextPath,
            isSameUserAsLoggedInUser,
            isLoggedInUserHasHigherOrEqualsRole,
            updateUserSetting,
            setConfiguration
        ]
    );

    return (
        <AuthContext.Provider value={context}>
            <DesignThemeProvider /*theme={userTheme}*/ {...props} />
        </AuthContext.Provider>
    );
};

AuthProvider.defaultProps = {
    authService: defaultAuthService,
    interopService: defaultInteropService,
    userSettingService: defaultUserSettingService
};

export default AuthProvider;
