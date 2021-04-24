import DataObject from '../entities/DataObject';
import DataObjectContainer from '../entities/DataObjectContainer';
import DataObjectFile from '../entities/DataObjectFile';

export default interface DataObjectRepository {
    create(container: DataObjectContainer): Promise<DataObject[]>;

    import(dataObjects: DataObject[]): Promise<DataObject[]>;

    validate(dataObjectContainer: DataObjectContainer): Promise<DataObjectFile[]>;

    update(dataObject: DataObject): Promise<void>;

    delete(id: string): Promise<void>;

    findAll(): Promise<DataObject[]>;

    findById(id: string): Promise<DataObject>;
}
