import axios from 'axios';
import swal from 'sweetalert2';
import fs from 'file-saver';
import Method from '@/modules/types/Method';
import FileInfo from '@/modules/types/FileInfo';
import MethodDef from '@/modules/types/MethodDef';
import RestApi from '@/modules/types/RestApi';
import RestApiMethod from '@/modules/types/RestApiMethod';
import { MethodType } from '@/modules/types/MethodType';

class DefinitionState {
  public currMethod: Method;
  public results: string;
  public file: FileInfo;
  constructor() {
    this.currMethod = new Method();
    this.results = '';
    this.file = new FileInfo();
  }
}

const getMethods = (jsonData: any): MethodDef[] => {
  const defs: MethodDef[] = [];
  jsonData.services.forEach((srv: any) => {
    srv.methods.forEach((mthd: any) => defs.push(new MethodDef(jsonData.baseURL, jsonData.modelName, srv.serviceName, srv.version, srv.transport,
      new Method(mthd.baseUrl, mthd.service, mthd.transport, mthd.methodName, mthd.version, mthd.variation, mthd.parms, mthd.retVals), MethodType.Method)));
    srv.notifications.forEach((mthd: any) => defs.push(new MethodDef(jsonData.baseURL, jsonData.modelName, srv.serviceName, srv.version, srv.transport,
      new Method(mthd.baseUrl, mthd.service, mthd.transport, mthd.methodName, mthd.version, mthd.variation, mthd.parms, mthd.retVals), MethodType.Notification)));
  });
  return defs;
};

const getMethodsFromRestApi = (jsonData: RestApi[]): MethodDef[] => {
  const defs: MethodDef[] = [];
  jsonData.forEach((srv: RestApi) => {
    srv.methods.forEach((m: RestApiMethod) => {
      defs.push(new MethodDef('', '', srv.serviceName, srv.version, '',
        new Method('', srv.serviceName, '', m.methodName, m.version, m.variation, m.parms, m.retVals), MethodType.Method));
    });
    srv.notifications.forEach((m: RestApiMethod) => {
      defs.push(new MethodDef('', '', srv.serviceName, srv.version, '',
        new Method('', srv.serviceName, '', m.methodName, m.version, m.variation, m.parms, m.retVals), MethodType.Notification));
    });
  });

  return defs;
};

export default class DefinitionModule {
  public namespaced: boolean = true;
  public state: DefinitionState = new DefinitionState();
  public mutations: any = {
    showResults(state: DefinitionState, res: string) {
      state.results = res;
    },
    deleteMethod(state: DefinitionState, idx: number) {
      state.file.methods.splice(idx, 1);
    },
    selectMethod(state: DefinitionState, idx: number) {
      state.file.selectedIdx = idx;

      if (idx < state.file.methods.length) {
        const mthd = state.file.methods[idx];
        if (mthd.methodType === MethodType.Method) {
          state.currMethod.baseUrl = mthd.baseUrl;
          state.currMethod.service = mthd.serviceName;
          state.currMethod.transport = mthd.transport;
          state.currMethod.command = mthd.method.command;
          state.currMethod.version = mthd.method.version;
          state.currMethod.parms = mthd.method.parms;
        }
      }
    },
    loadFile(state: DefinitionState, payload: any) {
      const jsonData: any = JSON.parse(payload.result);
      const defs: MethodDef[] = getMethods(jsonData);
      state.file.selectedIdx = -1;
      state.file.loadedFile = jsonData.modelName;
      state.file.setMethods(defs);
      swal.fire('Info', `Loaded ${defs.length} methods`);
    },
    loadRestFile(state: DefinitionState, payload: any) {
      const jsonData: any = JSON.parse(payload.result);
      const defs: MethodDef[] = getMethodsFromRestApi(jsonData);
      state.file.selectedIdx = -1;
      state.file.loadedFile = 'restapi';
      state.file.setMethods(defs);
      swal.fire('Info', `Loaded ${defs.length} methods`);
    },
    mergeFile(state: DefinitionState, payload: any) {
      const jsonData = JSON.parse(payload.result);
      if (!state.file.loadedFile.includes(jsonData.modelName)) {
        const defs = getMethods(jsonData);
        state.file.selectedIdx = -1;
        state.file.mergeMethods(defs);

        if (state.file.loadedFile === '') {
          state.file.loadedFile = jsonData.modelName;
        } else {
          state.file.loadedFile = state.file.loadedFile + ',' + jsonData.modelName;
        }
        swal.fire('Info', `Loaded ${defs.length} methods`);
      } else {
        swal.fire('Info', `Already loaded ${jsonData.modelName} methods`);
      }
    },
    saveFile(state: DefinitionState) {
      const services = new Map<string, RestApi>();
      state.file.methods.forEach((m) => {
        const srv: RestApi | undefined = services.get(m.serviceName);

        if (srv === undefined) {
          services.set(m.serviceName, new RestApi(m));
        } else {
          if (m.methodType === MethodType.Method) {
            srv.methods.push(new RestApiMethod(m.method));
          } else {
            srv.notifications.push(new RestApiMethod(m.method));
          }
        }
      });

      const restApi = [...services.values()];
      const blb = new Blob([JSON.stringify(restApi)], { type: 'application/json' });
      fs.saveAs(blb, 'restapi.json');
    },
  };

  public actions: any = {
    runCommand(context: any) {
      context.commit('showResults', 'waiting...');
      let parms = context.state.currMethod.parms;
      if (Array.isArray(parms)) {
        parms = parms.join(',');
      }

      axios.post('/sony/app/execute', {
        baseUrl: context.state.currMethod.baseUrl,
        serviceName: context.state.currMethod.service,
        transport: context.state.currMethod.transport,
        command: context.state.currMethod.command,
        version: context.state.currMethod.version,
        parms,
      }).then((res: any) => {
        if (res.data.success === true) {
          context.commit('showResults', res.data.results);
        } else {
          context.commit('showResults', res.data.message);
        }
      }, (res: any) => {
        const msg: string = res.response.status + ' ' + res.response.statusText;
        swal.fire('Error', msg, 'error');
        context.commit('showResults', msg);
      });
    },
  };
}
