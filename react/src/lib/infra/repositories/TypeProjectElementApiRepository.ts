import TypeProjectElement from '../../domain/entities/TypeProjectElement';
import TypeProjectElementRepository from '../../domain/repositories/TypeProjectElementRepository';
import { ApiGateway } from '../config/apiGateway';

export default class TypeProjectElementApiRepository implements TypeProjectElementRepository {
    readonly URL: string = '/typeprojectelements';

    constructor(private api: ApiGateway) {
    }

    async findAll(): Promise<TypeProjectElement[]> {
        return this.api.getArray(this.URL, { outType: TypeProjectElement });
    }
}