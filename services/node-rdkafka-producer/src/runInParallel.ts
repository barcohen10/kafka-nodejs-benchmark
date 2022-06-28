export default (fn, numberOfExecutions: number) =>
    Promise.allSettled(
        Array.from(Array(numberOfExecutions).keys()).map(() => fn())
    );