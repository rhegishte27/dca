import DataObjectLocationType from '../entities/DataObjectLocationType';

export default interface DataObjectLocationTypeRepository {
    findAll(): Promise<DataObjectLocationType[]>;
}