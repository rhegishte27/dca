import TypeProjectElement from '../entities/TypeProjectElement';

export default interface TypeProjectElementRepository {
    findAll(): Promise<TypeProjectElement[]>;
}