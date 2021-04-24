import Role from '../domain/entities/Role';
import RoleRepository from '../domain/repositories/RoleRepository';

export default class RoleService {
    constructor(private roleRepository: RoleRepository) {
    }

    async findAll(): Promise<Role[]> {
        return this.roleRepository.findAll();
    }
}