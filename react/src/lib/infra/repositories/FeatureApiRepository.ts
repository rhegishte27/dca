import Feature from '../../domain/entities/Feature';
import FeatureRepository from '../../domain/repositories/FeatureRepository';
import { ApiGateway } from '../config/apiGateway';

export default class FeatureApiRepository implements FeatureRepository {
    readonly URL: string = '/features';

    constructor(private api: ApiGateway) {
    }

    async findAll(): Promise<Feature[]> {
        return this.api.getArray(this.URL, {outType: Feature});
    }
}
