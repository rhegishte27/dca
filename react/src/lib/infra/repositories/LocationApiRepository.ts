import FileData from '../../domain/entities/FileData';
import Location from '../../domain/entities/Location';
import LocationRepository from '../../domain/repositories/LocationRepository';
import { ApiGateway } from '../config/apiGateway';

export default class LocationApiRepository implements LocationRepository {
    readonly URL: string = '/locations';

    constructor(private api: ApiGateway) {
    }

    async add(location: Location): Promise<Location> {
        return this.api.post(this.URL, location, { inType: Location, outType: Location });
    }

    async update(location: Location): Promise<void> {
        return this.api.put(this.URL + '/' + location.id, location, { inType: Location });
    }

    async delete(id: string): Promise<void> {
        return this.api.delete(this.URL + '/' + id);
    }

    async findAll(): Promise<Location[]> {
        return this.api.getArray(this.URL, { outType: Location });
    }

    async findById(id: string): Promise<Location> {
        return this.api.get(this.URL + '/' + id, { outType: Location });
    }

    async getFiles(id: string): Promise<FileData[]> {
        return this.api.getArray(this.URL + '/' + id + '/files', { outType: FileData });
    }
}
