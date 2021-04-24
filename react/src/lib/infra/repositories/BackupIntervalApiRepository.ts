import BackupInterval from '../../domain/entities/BackupInterval';
import BackupIntervalRepository from '../../domain/repositories/BackupIntervalRepository';
import {ApiGateway} from '../config/apiGateway';

export default class BackupIntervalApiRepository implements BackupIntervalRepository {
    readonly URL: string = '/backupintervals';

    constructor(private api: ApiGateway) {
    }

    async findAll(): Promise<BackupInterval[]> {
        return this.api.getArray(this.URL, {outType: BackupInterval});
    }
}