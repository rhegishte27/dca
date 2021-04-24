import FileData from '../domain/entities/FileData';
import Location from '../domain/entities/Location';
import LocationRepository from '../domain/repositories/LocationRepository';

export default class LocationService {
    constructor(private repository: LocationRepository) {
    }

    async add(location: Location): Promise<Location> {
        return this.repository.add(location);
    }

    async update(location: Location): Promise<void> {
        return this.repository.update(location);
    }

    async delete(id: string): Promise<void> {
        return this.repository.delete(id);
    }

    async findAll(): Promise<Location[]> {
        return this.repository.findAll();
    }

    async findById(id: string): Promise<Location> {
        return this.repository.findById(id);
    }

    async getFiles(id: string): Promise<FileData[]> {
        return this.repository.getFiles(id);
    }
}