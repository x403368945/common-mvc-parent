export const query = (connection, sql) => new Promise((resolve, reject) => {
    console.info(sql);
    connection.query(sql, (err, result) => {
        if (err) {
            console.error('[ERROR] - ', err.message);
            reject(err);
        } else {
            resolve(result);
        }
    })
});
export default {
    query
}
