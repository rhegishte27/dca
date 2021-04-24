import DataObject from '../../domain/entities/DataObject';
import DataObjectContainer from '../../domain/entities/DataObjectContainer';
import DataObjectFile from '../../domain/entities/DataObjectFile';
import DataObjectRepository from '../../domain/repositories/DataObjectRepository';
import { ApiGateway } from '../config/apiGateway';

export default class DataObjectApiRepository implements DataObjectRepository {
    readonly URL: string = '/dataobjects';

    constructor(private api: ApiGateway) {
    }

    async create(container: DataObjectContainer): Promise<DataObject[]> {
        return this.api.postReturnArray(this.URL + '/dataobjectstocreate', container, { inType: DataObjectContainer, outType: DataObject });
    }

    async import(dataObjects: DataObject[]): Promise<DataObject[]> {
        return this.api.postReturnArray(this.URL + '/dataobjectstoimport', dataObjects, { inType: DataObject, outType: DataObject });
    }

    async validate(dataObjectContainer: DataObjectContainer): Promise<DataObjectFile[]> {
        return this.api.postReturnArray(this.URL + '/validatedataobjects', dataObjectContainer, { inType: DataObjectContainer, outType: DataObjectFile });
    }

    async update(dataObject: DataObject): Promise<void> {
        return this.api.put(this.URL + '/' + dataObject.id, dataObject, { inType: DataObject });
    }

    async delete(id: string): Promise<void> {
        return this.api.delete(this.URL + '/' + id);
    }

    async findAll(): Promise<DataObject[]> {
        return this.api.getArray(this.URL, { outType: DataObject });
    }

    async findById(id: string): Promise<DataObject> {
        return this.api.get(this.URL + '/' + id, { outType: DataObject });
    }
}
