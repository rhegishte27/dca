import Configuration from '../../domain/entities/Configuration';
import LoginRequest from '../../domain/entities/loginRequest';
import AuthRepository from '../../domain/repositories/authRepository';
import {ApiGateway} from '../config/apiGateway';

export default class AuthApiRepository implements AuthRepository {
    constructor(private api: ApiGateway) {}

    setDisconnectHandler(onDisconnect: () => void): void {
        this.api.setDisconnectHandler(onDisconnect);
    }

    async login(identifier: string, password: string): Promise<Configuration> {
        const request: LoginRequest = {identifier: identifier, password};
        return this.api.post('/login', request, {inType: LoginRequest, outType: Configuration});
    }

    async logout(): Promise<void> {
        return this.api.post('/logout', null);
    }
}
