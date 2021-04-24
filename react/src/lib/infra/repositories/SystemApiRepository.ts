import System from '../../domain/entities/System';
import SystemRepository from '../../domain/repositories/SystemRepository';
import {ApiGateway} from '../config/apiGateway';

export default class SystemApiRepository implements SystemRepository {
    readonly URL: string = '/systems';

    constructor(private api: ApiGateway) {
    }

    async add(system: System): Promise<System> {
        return this.api.post(this.URL, system, {inType: System, outType: System});
    }

    async update(system: System): Promise<void> {
        return this.api.put(this.URL + '/' + system.id, system, {inType: System});
    }

    async delete(id: string): Promise<void> {
        return this.api.delete(this.URL + '/' + id);
    }

    async findAll(): Promise<System[]> {
        return this.api.getArray(this.URL, {outType: System});
    }

    async findById(id: string): Promise<System> {
        return this.api.get(this.URL + '/' + id, {outType: System});
    }
}
