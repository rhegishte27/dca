import BackupInterval from '../domain/entities/BackupInterval';
import BackupIntervalRepository from '../domain/repositories/BackupIntervalRepository';

export default class BackupIntervalService {
    constructor(private backupIntervalRepository: BackupIntervalRepository) {
    }

    async findAll(): Promise<BackupInterval[]> {
        return this.backupIntervalRepository.findAll();
    }
}