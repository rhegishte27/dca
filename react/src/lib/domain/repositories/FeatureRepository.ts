import Feature from '../entities/Feature';

export default interface FeatureRepository {
    findAll(): Promise<Feature[]>;
    
}