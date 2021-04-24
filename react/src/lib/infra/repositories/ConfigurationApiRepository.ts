import Configuration from '../../domain/entities/Configuration';
import ConfigurationRepository from '../../domain/repositories/ConfigurationRepository';
import {ApiGateway} from '../config/apiGateway';

export default class ConfigurationApiRepository implements ConfigurationRepository {
    readonly URL: string = '/configurations';

    constructor(private api: ApiGateway) {
    }

    async get(): Promise<Configuration> {
        return this.api.get(this.URL, {outType: Configuration});
    }
}