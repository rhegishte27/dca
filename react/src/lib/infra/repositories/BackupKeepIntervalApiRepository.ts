import BackupKeepInterval from '../../domain/entities/BackupKeepInterval';
import BackupKeepIntervalRepository from '../../domain/repositories/BackupKeepIntervalRepository';
import {ApiGateway} from '../config/apiGateway';

export default class BackupKeepIntervalApiRepository implements BackupKeepIntervalRepository {
    readonly URL: string = '/backupkeepintervals';

    constructor(private api: ApiGateway) {
    }

    async findAll(): Promise<BackupKeepInterval[]> {
        return this.api.getArray(this.URL, {outType: BackupKeepInterval});
    }
}