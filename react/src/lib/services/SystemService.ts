import System from '../domain/entities/System';
import SystemRepository from '../domain/repositories/SystemRepository';

export default class SystemService {
    constructor(private repository: SystemRepository) {
    }

    async add(system: System): Promise<System> {
        return this.repository.add(system);
    }

    async update(system: System): Promise<void> {
        return this.repository.update(system);
    }

    async delete(id: string): Promise<void> {
        return this.repository.delete(id);
    }

    async findAll(): Promise<System[]> {
        return this.repository.findAll();
    }

    async findById(id: string): Promise<System> {
        return this.repository.findById(id);
    }
}