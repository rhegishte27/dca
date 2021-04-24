import Role from '../../domain/entities/Role';
import RoleRepository from '../../domain/repositories/RoleRepository';
import { ApiGateway } from '../config/apiGateway';

export default class RoleApiRepository implements RoleRepository {
    readonly URL: string = '/roles';

    constructor(private api: ApiGateway) {
    }

    async findAll(): Promise<Role[]> {
        return this.api.getArray(this.URL, {outType: Role});
    }
}