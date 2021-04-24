import Compiler from '../../domain/entities/Compiler';
import CompilerRepository from '../../domain/repositories/CompilerRepository';
import {ApiGateway} from '../config/apiGateway';

export default class CompilerApiRepository implements CompilerRepository {
    readonly URL: string = '/compilers';

    constructor(private api: ApiGateway) {
    }

    async findAll(): Promise<Compiler[]> {
        return this.api.getArray(this.URL, {outType: Compiler});
    }
}