import Configuration from '../entities/Configuration';

export default interface AuthRepository {
    setDisconnectHandler(onDisconnect: () => void): void;

    login(userName: string, password: string): Promise<Configuration>;
    logout(): Promise<void>;
}
