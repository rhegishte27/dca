import FileData from '../entities/FileData';
import Location from '../entities/Location';

export default interface LocationRepository {

    add(location: Location): Promise<Location>;

    update(location: Location): Promise<void>;

    delete(id: string): Promise<void>;

    findAll(): Promise<Location[]>;

    findById(id: string): Promise<Location>;

    getFiles(id: string): Promise<FileData[]>;
}
