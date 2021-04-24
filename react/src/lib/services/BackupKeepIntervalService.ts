import BackupKeepInterval from '../domain/entities/BackupKeepInterval';
import BackupKeepIntervalRepository from '../domain/repositories/BackupKeepIntervalRepository';

export default class BackupKeepIntervalService {
    constructor(private backupKeepIntervalRepository: BackupKeepIntervalRepository) {
    }

    async findAll(): Promise<BackupKeepInterval[]> {
        return this.backupKeepIntervalRepository.findAll();
    }
}