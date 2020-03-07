/**
 * 文件路径操作
 * @author 谢长春 2019-7-28
 */
import fs from 'fs';
import path from 'path';

const mkdirs = function (dir) {
  fs.mkdirSync(dir, {recursive: true});
  return dir;
};

const copy = function (src, dist) {
  fs.readdirSync(src, 'utf8').forEach(file => {
    if (fs.statSync(path.join(src, file)).isFile()) {
      fs.copyFileSync(path.join(src, file), path.join(dist, file));
    } else {
      copy(path.join(src, file), Paths.resolve(dist, file).mkdirs().absolute());
    }
  });
  return dist;
};
const remove = function (src) {
  fs.readdirSync(src, 'utf8').forEach(file => {
    if (fs.statSync(path.join(src, file)).isDirectory()) {
      remove(path.join(src, file));
      fs.rmdirSync(path.join(src, file));
    } else fs.unlinkSync(path.join(src, file));
  });
};
export default class Paths {
  static resolve(...args) {
    return new Paths(path.resolve(args.join(path.sep)));
  }

  constructor(absolute) {
    this._absolute = absolute;
  }

  /**
   * 上级目录对象
   * @returns {Paths}
   */
  parent() {
    return new Paths(path.dirname(this._absolute));
  }

  /**
   * 绝对路径
   * @returns {String}
   */
  absolute() {
    return this._absolute;
  }

  /**
   * 追加路径
   * @param name {String} 文件名|目录名
   * @returns {Paths}
   */
  append(name) {
    this._absolute = path.resolve(this._absolute, name);
    return this;
  }

  /**
   * 创建目录
   * @returns {Paths}
   */
  mkdirs() {
    mkdirs(this._absolute);
    return this;
  }

  /**
   * 创建上级目录
   * @returns {Paths}
   */
  mkdirsParent() {
    mkdirs(path.dirname(this._absolute));
    return this;
  }

  /**
   * 设置权限为755
   * @returns {Paths}
   */
  chmod755() {
    return this.chmod(755);
  }

  /**
   * 设置权限
   * @param mode
   * @returns {Paths}
   */
  chmod(mode = 644) {
    fs.chmodSync(this._absolute, mode);
    return this;
  }

  /**
   * 检测路径是否存在
   * @returns {boolean}
   */
  exist() {
    return fs.existsSync(this._absolute);
  }

  /**
   * 写入内容
   * @param content {String}
   * @returns {Paths}
   */
  write(content) {
    fs.writeFileSync(this._absolute, content, {mode: 644});
    return this;
  }

  /**
   * 将对象格式化成JSON字符串后写入文件
   * @param content {*}
   * @returns {Paths}
   */
  writeJSON(content) {
    return this.write(JSON.stringify(content, null, 2));
  }

  /**
   * 读取文件内容
   * @returns {Promise<String>}
   */
  read() {
    return new Promise((resolve, reject) => {
      fs.readFile(
        this._absolute,
        (err, content) => {
          if (err) reject(err);
          else resolve(content.toString());
        });
    });
  }

  /**
   * 读取文件内容
   * @returns {String}
   */
  readSync() {
    return fs.readFileSync(this._absolute).toString();
  }

  /**
   * 读取文件内容，并 JSON.parse
   * @returns {Promise<any>}
   */
  readJson() {
    return new Promise((resolve, reject) => {
      fs.readFile(
        this._absolute,
        (err, content) => {
          if (err) reject(err);
          else resolve(JSON.parse(content.toString()));
        });
    });
  }

  /**
   * 读取文件内容，并 JSON.parse
   * @returns {any}
   */
  readJsonSync() {
    const buffer = fs.readFileSync(this._absolute);
    return (buffer) ? JSON.parse(buffer.toString()) : null
  }

  /**
   * 复制文件
   * @param dists {Array} 文件目录
   * @returns {String}
   */
  copy(...dists) {
    if (fs.statSync(this._absolute).isFile()) {
      const dist = path.resolve(dists.join(path.sep));
      fs.copyFileSync(this._absolute, dist);
      return dist;
    }
    return copy(this._absolute, Paths.resolve(dists.join(path.sep)).mkdirs().absolute());
  }

  /**
   * 递归删除删除目录
   * @param self {Boolean} true：删除本身，false：不删除本身
   * @returns {Boolean}
   */
  rm(self = false) {
    if (!fs.existsSync(this._absolute)) return true;
    if (fs.statSync(this._absolute).isFile()) {
      fs.unlinkSync(this._absolute);
      return true;
    }
    remove(this._absolute);
    // 当为目录时，是否删除目录本身
    if (self) fs.rmdirSync(this._absolute);
    return true;
  }
}
