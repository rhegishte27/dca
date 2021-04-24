import Configuration from '../domain/entities/Configuration';
import AuthRepository from '../domain/repositories/authRepository';

export default class AuthService {
    constructor(private authRepository: AuthRepository) {}

    setDisconnectHandler(onDisconnect: () => void): void {
        this.authRepository.setDisconnectHandler(onDisconnect);
    }

    login = async (identifier: string, password: string): Promise<Configuration> => {
        return this.authRepository.login(identifier, password);
    };

    logout = async () => {
        return this.authRepository.logout();
    };

}
