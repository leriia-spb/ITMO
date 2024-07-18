package search;


// sort: for i = 1 ... n - 1: ints[i] >= ints[i + 1]
public class BinarySearch {
    // sort && ints[n - 1] <= x
    public static Integer binSearchIteration(final int x, final int[] ints) {
        // sort && ints[n - 1] <= x
        int l = 0;
        // sort && l == 0 && a[n - 1] <= x
        int r = ints.length;
        // sort && r == ints.length && l == 0 && l <= r && a[n - 1] <= x

        // Inv: l < r && sort && (ints[l] >= x >= ints[r] || x >= ints[l] >= ints[r]) && (l == 0 || ints[l - 1] > x)
        while (l < r) {
            // l < r && sort && (ints[l] >= x >= ints[r] || x >= ints[l] >= ints[r]) && (l == 0 || ints[l - 1] > x)
            int mid = (l + r) / 2;
            // l < r && sort && (ints[l] >= x >= ints[r] || x >= ints[l] >= ints[r])
            // && mid == (l + r) / 2 && r > mid >= l
            if (ints[mid] <= x) {
                // l < r && sort && (ints[l] >= x >= ints[r] || x >= ints[l] >= ints[r])
                // && mid == (l + r) / 2 && r > mid >= l && x >= ints[mid] && (l == 0 || ints[l - 1] > x)

                // l < r && sort &&
                // (ints[l] >= x >= ints[mid] >= ints[r]  || x >= ints[l] >= ints[mid] >= ints[r])
                // && mid == (l + r) / 2 && r > mid >= l && (l == 0 || ints[l - 1] > x)

                // l < r && sort &&
                // (ints[l] >= x >= ints[mid]  || x >= ints[l] >= ints[mid]) && r > mid >= l
                // && (l == 0 || ints[l - 1] > x)
                r = mid;
                // (ints[l] >= x >= ints[r]  || x >= ints[l] >= ints[r]) && sort && r == mid
                // && (l == 0 || ints[l - 1] > x)

                // (r - l) == (r' - l) / 2 - ???
                // r - l = (r' + l) / 2 - l = (r' - l) / 2

            } else {
                // l < r && sort && ints[l] >= x >= ints[r]
                // && mid == (l + r) / 2 && r > mid >= l && x < ints[mid] && (l == 0 || ints[l - 1] > x)

                // l < r && sort &&
                // ints[l] >= ints[mid] > x >= ints[mid] >= ints[r]
                // && mid == (l + r) / 2 && r > mid >= l && (l == 0 || ints[l - 1] > x)

                // l < r && sort &&
                // ints[mid] > x >= ints[r] && r > mid >= l && (l == 0 || ints[l - 1] > x)
                l = mid + 1;
                // ints[l] > x >= ints[r] && sort && l == mid && ints[l - 1] > x

                // (r - l) == (r - l') / 2 - ???
                // r - l = r - (r + l) / 2 + 1 = (r - l') / 2 + 1
            }
            // (ints[l] >= x >= ints[r] ||  x >= ints[l] >= ints[r]) && sort && (l == 0 || ints[l - 1] > x)

            // r', l' -> r, l
            // ((r - l) == (r - l') / 2 + 1) || ((r - l) == (r' - l) / 2)
            // T(n) = T(n/2)+O(1),n > 1; O(1),n=1 => T(n) = O(log n) --- Master theorem
        }
        // sort && l == r && (ints[l] == x == ints[r] || x >= ints[l] == ints[r]) && (l == 0 || ints[l - 1] > x)

        // O(log2(ints.length))= O(log n)
        return l;
    }


    // && ints[n - 1] <= x && l <= r && && (ints[l] >= x >= ints[r] || x >= ints[l] >= ints[r])
    // && (l == 0 || ints[l - 1] > x) && sort

    // T(n) = T(n/2)+O(1),n > 1; O(1),n=1 => T(n) = O(log n) --- Master theorem
    public static Integer binSearchRecursion(final int x, final int[] ints, int l, int r) {
        // sort && ints[n - 1] <= x && l <= r && && (ints[l] >= x >= ints[r] || x >= ints[l] >= ints[r])
        // && (l == 0 || ints[l - 1] > x)
        if (l == r) {
            // sort && l == r && (ints[l] == x == ints[r] || x >= ints[l] == ints[r]) && (l == 0 || ints[l - 1] > x)
            return r;
        }
        // l < r && sort && (ints[l] >= x >= ints[r] || x >= ints[l] >= ints[r]) && (l == 0 || ints[l - 1] > x)
        int mid = (l + r) / 2;
        // l < r && sort && (ints[l] >= x >= ints[r] || x >= ints[l] >= ints[r])
        // && mid == (l + r) / 2 && r >= mid >= l
        if (ints[mid] <= x) {
            // l < r && sort && (ints[l] >= x >= ints[r] || x >= ints[l] >= ints[r])
            // && mid == (l + r) / 2 && r >= mid >= l && x >= ints[mid] && (l == 0 || ints[l - 1] > x)

            // l < r && sort &&
            // (ints[l] >= x >= ints[mid] >= ints[r]  || x >= ints[l] >= ints[mid] >= ints[r])
            // && mid == (l + r) / 2 && r >= mid >= l && (l == 0 || ints[l - 1] > x)

            // l < r && sort &&
            // (ints[l] >= x >= ints[mid]  || x >= ints[l] >= ints[mid]) && r >= mid >= l
            // && (l == 0 || ints[l - 1] > x)
            return binSearchRecursion(x, ints, l, mid);
            // (r - l) == (r' - l) / 2 - ???
            // r - l = (r' + l) / 2 - l = (r' - l) / 2

            // (ints[l] >= x >= ints[r]  || x >= ints[l] >= ints[r]) && sort && r == mid
            // && (l == 0 || ints[l - 1] > x)
        } else {
            // l < r && sort && ints[l] >= x >= ints[r]
            // && mid == (l + r) / 2 && r >= mid >= l && x < ints[mid] && (l == 0 || ints[l - 1] > x)

            // l < r && sort &&
            // ints[l] >= ints[mid] > x >= ints[mid] >= ints[r]
            // && mid == (l + r) / 2 && r >= mid >= l && (l == 0 || ints[l - 1] > x)

            // l < r && sort &&
            // ints[mid] > x >= ints[r] && r >= mid >= l && (l == 0 || ints[l - 1] > x)
            return binSearchRecursion(x, ints, mid + 1, r);
            // (r - l) == (r - l') / 2 - ???
            // r - l = r - (r + l) / 2 + 1 = (r - l') / 2 + 1
            // ints[l] > x >= ints[r] && sort && l == mid && ints[l - 1] > x
        }
    }

    // for i = 1... args.length - 1: Integer.parseInt(args[0]) && sort && Integer.parseInt(args[0])
    public static void main(String[] args) {
        // True
        if (args.length > 0) {
            // args.length > 0
            int x = Integer.parseInt(args[0]);
            // args.length > 0 && x == Integer.parseInt(args[0])
            int[] nums = new int[args.length - 1];
            // args.length > 0 && x == Integer.parseInt(args[0]) && nums = new int[args.length - 1]

            // args.length > 0 && x == Integer.parseInt(args[0]) &&
            // nums = new int[args.length - 1] && i == 1

            // Inv: args.length > 0 && x == Integer.parseInt(args[0]) &&
            // nums = new int[args.length - 1] && i < args.length
            for (int i = 1; i < args.length; i++) {
                // args.length > 0 && x == Integer.parseInt(args[0]) &&
                //i < args.length && i == i'
                nums[i - 1] = Integer.parseInt(args[i]);
                // args.length > 0 && x == Integer.parseInt(args[0]) && i == i' && (i == 1 || nums[i - 2] > nums[i - 1])
                // i < args.length && nums[i - 1] = Integer.parseInt(args[i])

                // args.length > 0 && x == Integer.parseInt(args[0]) &&
                // i == i' + 1 && (i == 2 || nums[i - 2] > nums[i - 1])
            }
            // args.length > 0 && x == Integer.parseInt(args[0]) && sort
            System.out.println(binSearchIteration(x, nums));
//            System.out.println(binSearchRecursion(x, nums, 0, nums.length));
            // args.length > 0 && x == Integer.parseInt(args[0]) && sort
            // (rez == 0 || nums[rez - 1] > x) && nums[rez] <= x
        } else {
            // args.length == 0
            System.err.println("Too few arguments");
        }
    }
}
