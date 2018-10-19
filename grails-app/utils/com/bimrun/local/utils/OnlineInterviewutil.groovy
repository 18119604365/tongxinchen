package com.bimrun.local.utils

class OnlineInterviewutil {


    static List<Long> getRandomSubjects(List<Long> ids, int count) {
        int index = 0
        int size = ids.size();
        if (ids.size() < count) {
            return null;
        } else {
            Random random = new Random();
            for (int i = 0; i < count; i++) {
                //获取随机下标
                index = random.nextInt(size - i);
                //给randomIds赋值
                ids.add(ids.get(index));
                //随机的下标的元素和位置最后一位没有使用过的元素换取位置
                ids.set(index, ids.get(size - i - 1))
                ids.set(ids.size() - i - 1, ids.get(size + i))
//                ids[index] = ids[size - i - 1];
//                ids[ids.length - i - 1] = randomIds.get(i)
            }
            return ids.subList(size, size + count - 1);
        }

    }
}

