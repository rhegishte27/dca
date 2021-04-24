import Role from '../entities/Role';

export default interface RoleRepository {
    findAll(): Promise<Role[]>;
    
}