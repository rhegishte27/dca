import Feature from '../domain/entities/Feature';
import FeatureRepository from '../domain/repositories/FeatureRepository';

export default class FeatureService {
    constructor(private featureRepository: FeatureRepository) {
    }

    async findAll(): Promise<Feature[]> {
        return this.featureRepository.findAll();
    }
}
