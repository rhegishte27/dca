import System from '../entities/System';

export default interface SystemRepository {

    add(system: System): Promise<System>;

    update(system: System): Promise<void>;

    delete(id: string): Promise<void>;

    findAll(): Promise<System[]>;

    findById(id: string): Promise<System>;
}
