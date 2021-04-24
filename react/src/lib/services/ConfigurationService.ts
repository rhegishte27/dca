import Configuration from '../domain/entities/Configuration';
import ConfigurationRepository from '../domain/repositories/ConfigurationRepository';

export default class ConfigurationService {
    constructor(private configurationRepository: ConfigurationRepository) {
    }

    async get(): Promise<Configuration> {
        return this.configurationRepository.get();
    }
}