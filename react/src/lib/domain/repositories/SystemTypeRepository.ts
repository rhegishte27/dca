import SystemType from '../entities/SystemType';

export default interface SystemTypeRepository {
    findAll(): Promise<SystemType[]>;
}
