package search;
// O(log n) main sort(d)
// unique: ints[i] == ints[j] <=> i == j
// sort(d): E d: for i = 0 ... d ints[i] < ints[i + 1] && for i = d + 1 ... n - 1 ints[i] < ints[i + 1]
// && ints[d + 1] < ints[0]
public class BinarySearchShift {
    // unique && sort(d)
    public static Integer binSearchIteration(final int x, final int[] ints, final int d) {
        // unique && sort(d)
        int l = 0;
        // unique && sort(d) && l == 0
        int r = ints.length;
        // unique && sort(d) && l == 0 && r == ints.length

        // DF: l' == (l + d) % ints.length && r' == (r + d) % ints.length && n == ints.lenght

        // Inv: unique && sort(d) && l < r &&
        // (ints[l'] <= x <= ints[r'] || x <= ints[l'] <= ints[r'] || ints[l'] <= ints[r'] <= x)
        // && (r' == n - 1 || ints[r' + 1] > x) && (l' == 0 || ints[l' - 1] < x)
        while (l < r) {
            // unique && sort(d) && l < r && (r' == n - 1 || ints[r' + 1] > x) && (l' == 0 || ints[l' - 1] < x)
            // (ints[l'] <= x <= ints[r'] || x <= ints[l'] <= ints[r'] || ints[l'] <= ints[r'] <= x)
            int mid = (l + r) / 2;
            // unique && sort(d) && l < r && mid == (l + r) / 2 &&
            // (ints[l'] <= x <= ints[r'] || x <= ints[l'] <= ints[r'] || ints[l'] <= ints[r'] <= x)
            // && (r' == n - 1 || ints[r' + 1] > x) && (l' == 0 || ints[l' - 1] < x)
            // DF: mid' = (mid + d) % ints.length
            if (ints[(mid + d) % ints.length] >= x) {
                // l < r && sort(d) && (ints[l'] <= x <= ints[r'] || x <= ints[l'] <= ints[r']) && mid == (l + r) / 2
                // && mid' == ((l + r) / 2 + d) % ints.length == (l + r + d + d) / 2 % ints.length == (l' + r') / 2
                // && x >= ints[mid'] && unique && (r' == n - 1 || ints[r' + 1] > x) && (l' == 0 || ints[l' - 1] < x)

                // l < r && sort(d) && unique &&
                // (ints[l'] <= x <= ints[mid'] <= ints[r']  || x <= ints[l'] <= ints[mid'] <= ints[r'])
                // && mid' == (l' + r') / 2 && r >= mid >= l
                // && (r' == n - 1 || ints[r' + 1] > x) && (l' == 0 || ints[l' - 1] < x)

                // l < r && sort(d) && unique &&
                // (ints[l'] <= x <= ints[mid'] || x <= ints[l'] <= ints[mid'])
                // && mid' == (l' + r') / 2 && r >= mid >= l
                // && (r' == n - 1 || ints[r' + 1] > x) && (l' == 0 || ints[l' - 1] < x)
                r = mid;
                // unique && (ints[l'] <= x <= ints[r']  || x <= ints[l'] <= ints[r']) && sort(d)
                // && r == mid && r' === mid' && (r' == n - 1 || ints[r' + 1] > x) && (l' == 0 || ints[l' - 1] < x)
            } else {
                // l < r && sort(d) && (ints[l'] <= x <= ints[r'] || ints[l'] <= ints[r'] < x) && mid == (l + r) / 2
                // && mid' == ((l + r) / 2 + d) % ints.length == (l + r + d + d) / 2 % ints.length == (l' + r') / 2
                // && x < ints[mid'] && unique && (r' == n - 1 || ints[r' + 1] > x) && (l' == 0 || ints[l' - 1] < x)

                // l < r && sort(d) && unique &&
                // (ints[l'] <= x <= ints[mid'] <= ints[r']  || ints[l'] <= ints[mid'] <= ints[r'] < x)
                // && mid' == (l' + r') / 2 && r >= mid >= l
                // && (r' == n - 1 || ints[r' + 1] > x) && (l' == 0 || ints[l' - 1] < x)

                // l < r && sort(d) && unique &&
                // (ints[l'] <= x <= ints[mid'] || ints[l'] <= ints[mid'] < x)
                // && mid' == (l' + r') / 2 && r >= mid >= l
                // && (r' == n - 1 || ints[r' + 1] > x) && (l' == 0 || ints[l' - 1] < x)
                l = mid + 1;
                // (ints[l'] < x <= ints[r'] || ints[l'] <= ints[mid'] < x) && sort(d) && l == mid  && l' == mid'
                // && unique && (r' == n - 1 || ints[r' + 1] > x) && (l' == 0 || ints[l' - 1] < x)
            }
            //unique  && (ints[l'] <= x <= ints[r'] ||  x <= ints[l'] <= ints[r'] || ints[l'] <= ints[r'] <= x)
            // && sort(d) && (r' == n - 1 || ints[r' + 1] > x) && (l' == 0 || ints[l' - 1] < x)
        }
        // unique && l == r && l' == r' && sort(d)
        // && (ints[l'] <= x <= ints[r'] ||  x <= ints[l'] <= ints[r'] || ints[l'] <= ints[r'] <= x)
        // && (r' == n - 1 || ints[r' + 1] > x) && (l' == 0 || ints[l' - 1] < x)

        // unique && l == r && l' == r' && sort(d)
        // && (ints[l'] == x == ints[r'] ||  x <= ints[l'] == ints[r'] || ints[l'] == ints[r'] <= x)
        // && (r' == n - 1 || ints[r' + 1] > x) && (l' == 0 || ints[l' - 1] < x)
        if (ints[(l + d) % ints.length] == x) {
            // unique && l == r && l' == r' && sort(d) && ints[l'] == x &&
            // && (ints[l'] == x == ints[r'] ||  x <= ints[l'] == ints[r'] || ints[l'] == ints[r'] <= x)
            // && (r' == n - 1 || ints[r' + 1] > x) && (l' == 0 || ints[l' - 1] < x)
            return (l + d) % ints.length;
            // unique && l == r && l' == r' && sort(d) && ints[l'] == x
        }
        // unique && l == r && l' == r' && sort(d) && ints[l'] != x &&
        // && (ints[l'] == x == ints[r'] ||  x <= ints[l'] == ints[r'] || ints[l'] == ints[r'] <= x)
        // && (r' == n - 1 || ints[r' + 1] > x) && (l' == 0 || ints[l' - 1] < x)

        // unique && l == r && l' == r' && sort(d) && ints[l'] != x &&
        // && (x <= ints[l'] == ints[r'] || ints[l'] == ints[r'] <= x) && (r' == n - 1 || ints[r' + 1] > x) &&
        // (l' == 0 || ints[l' - 1] < x)
        return -1;
        // for i = 1 ... n: x != ints[i]
    }
    // for i = 1 ... n: x != ints[i] || ints[rez] == x


    // DF: l' == (l + d) % ints.length && r' == (r + d) % ints.length && n == ints.lenght
    // Inv: unique && sort(d) && l <= r &&
    // (ints[l'] <= x <= ints[r'] || x <= ints[l'] <= ints[r'] || ints[l'] <= ints[r'] <= x)
    // && (r' == n - 1 || ints[r' + 1] > x) && (l' == 0 || ints[l' - 1] < x)
    public static Integer binSearchRecursion(final int x, final int[] ints, int l, int r, final int d) {
        // unique && sort(d) && l <= r &&
        // (ints[l'] <= x <= ints[r'] || x <= ints[l'] <= ints[r'] || ints[l'] <= ints[r'] <= x)
        // && (r' == n - 1 || ints[r' + 1] > x) && (l' == 0 || ints[l' - 1] < x)
        if (l == r) {
            // unique && sort(d) && l == r && l' == r' &&
            // (ints[l'] <= x <= ints[r'] || x <= ints[l'] <= ints[r'] || ints[l'] <= ints[r'] <= x)
            // && (r' == n - 1 || ints[r' + 1] > x) && (l' == 0 || ints[l' - 1] < x)

            // unique && sort(d) && l' == r' && l == r &&
            // (ints[l'] == x == ints[r'] || x <= ints[l'] == ints[r'] || ints[l'] == ints[r'] <= x)
            // && (r' == n - 1 || ints[r' + 1] > x) && (l' == 0 || ints[l' - 1] < x)
            if (ints[(l + d) % ints.length] == x) {
                // unique && l == r && l' == r' && sort(d) && ints[l'] == x &&
                // && (ints[l'] == x == ints[r'] ||  x == ints[l'] == ints[r'] || ints[l'] == ints[r'] == x)
                // && (r' == n - 1 || ints[r' + 1] > x) && (l' == 0 || ints[l' - 1] < x)
                return (l + d) % ints.length;
                // unique && l == r && l' == r' && sort(d) && ints[l'] == x == ints[r']
            }
            // unique && l == r && l' == r' && sort(d) && ints[l'] != x &&
            // && (ints[l'] == x == ints[r'] ||  x <= ints[l'] == ints[r'] || ints[l'] == ints[r'] <= x)
            // && (r' == n - 1 || ints[r' + 1] > x) && (l' == 0 || ints[l' - 1] < x)

            // unique && l == r && l' == r' && sort(d) && ints[l'] != x &&
            // && (x < ints[l'] == ints[r'] || ints[l'] == ints[r'] < x)
            // && (r' == n - 1 || ints[r' + 1] > x) && (l' == 0 || ints[l' - 1] < x)
            return -1;
            // for i = 1 ... n: x != ints[i]
        }
        // unique && sort(d) && l < r && (r' == n - 1 || ints[r' + 1] > x) && (l' == 0 || ints[l' - 1] < x)
        // (ints[l'] <= x <= ints[r'] || x <= ints[l'] <= ints[r'] || ints[l'] <= ints[r'] <= x)
        int mid = (l + r) / 2;
        // unique && sort(d) && l < r && mid == (l + r) / 2 &&
        // (ints[l'] <= x <= ints[r'] || x <= ints[l'] <= ints[r'] || ints[l'] <= ints[r'] <= x)
        // && (r' == n - 1 || ints[r' + 1] > x) && (l' == 0 || ints[l' - 1] < x)
        // DF: mid' = (mid + d) % ints.length
        if (ints[(mid + d) % ints.length] >= x) {
            // l < r && sort(d) && (ints[l'] <= x <= ints[r'] || x <= ints[l'] <= ints[r']) && mid == (l + r) / 2
            // && mid' == ((l + r) / 2 + d) % ints.length == (l + r + d + d) / 2 % ints.length == (l' + r') / 2
            // && x >= ints[mid'] && unique && (r' == n - 1 || ints[r' + 1] > x) && (l' == 0 || ints[l' - 1] < x)

            // l < r && sort(d) && unique &&
            // (ints[l'] <= x <= ints[mid'] <= ints[r']  || x <= ints[l'] <= ints[mid'] <= ints[r'])
            // && mid' == (l' + r') / 2 && r >= mid >= l
            // && (r' == n - 1 || ints[r' + 1] > x) && (l' == 0 || ints[l' - 1] < x)

            // l < r && sort(d) && unique &&
            // (ints[l'] <= x <= ints[mid'] || x <= ints[l'] <= ints[mid'])
            // && mid' == (l' + r') / 2 && r >= mid >= l
            // && (r' == n - 1 || ints[r' + 1] > x) && (l' == 0 || ints[l' - 1] < x)
            return binSearchRecursion(x, ints, l, mid, d);
            // unique && (ints[l'] <= x <= ints[r']  || x <= ints[l'] <= ints[r']) && sort(d)
            // && r == mid && r' === mid' && (r' == n - 1 || ints[r' + 1] > x) && (l' == 0 || ints[l' - 1] < x)
        } else {
            // l < r && sort(d) && (ints[l'] <= x <= ints[r'] || ints[l'] <= ints[r'] < x) && mid == (l + r) / 2
            // && mid' == ((l + r) / 2 + d) % ints.length == (l + r + d + d) / 2 % ints.length == (l' + r') / 2
            // && x < ints[mid'] && unique && (r' == n - 1 || ints[r' + 1] > x) && (l' == 0 || ints[l' - 1] < x)

            // l < r && sort(d) && unique &&
            // (ints[l'] <= x <= ints[mid'] <= ints[r']  || ints[l'] <= ints[mid'] <= ints[r'] < x)
            // && mid' == (l' + r') / 2 && r >= mid >= l
            // && (r' == n - 1 || ints[r' + 1] > x) && (l' == 0 || ints[l' - 1] < x)

            // l < r && sort(d) && unique &&
            // (ints[l'] <= x <= ints[mid'] || ints[l'] <= ints[mid'] < x)
            // && mid' == (l' + r') / 2 && r >= mid >= l
            // && (r' == n - 1 || ints[r' + 1] > x) && (l' == 0 || ints[l' - 1] < x)
            return binSearchRecursion(x, ints, mid + 1, r, d);
            // (ints[l'] < x <= ints[r'] || ints[l'] <= ints[mid'] < x) && sort(d) && l == mid  && l' == mid'
            // && unique && (r' == n - 1 || ints[r' + 1] > x) && (l' == 0 || ints[l' - 1] < x)
        }
    }
    // for i = 1 ... n: x != ints[i] || ints[rez] == x


    // unique && sort(d)
    public static Integer binSearchStartIteration(final int[] ints) {
        // unique && sort(d)
        int l = 0;
        // unique && sort(d) && l == 0
        int r = ints.length;
        // unique && sort(d) && l == 0  && r == ints.length && ints[l] - ints[0] == 0

        // Inv: l < r && unique && sort(d) && (l == 0 || ints[l - 1] < ints[l]) && (r >= n - 1 || ints[r + 1] > ints[r])
        // (ints[l] - ints[0] >= 0 && ints[r] - ints[0] < 0 || ints[l] - ints[0] >= 0 && ints[r] - ints[0] > 0
        // || r == n)
        while (l < r) {
            // l < r && unique && sort(d)
            // && (l == 0 || ints[l - 1] < ints[l]) && (r >= n - 1 || ints[r + 1] > ints[r]) &&
            // (ints[l] - ints[0] >= 0 && ints[r] - ints[0] < 0 || ints[l] - ints[0] >= 0 && ints[r] - ints[0] > 0)
            int mid = (l + r) / 2;
            // l <= mid < r && unique && sort(d) && mid = (l + r) / 2
            // && (l == 0 || ints[l - 1] < ints[l]) && (r >= n - 1 || ints[r + 1] > ints[r]) &&
            // (ints[l] - ints[0] >= 0 && ints[r] - ints[0] < 0 || ints[l] - ints[0] >= 0 && ints[r] - ints[0] > 0)
            if (ints[mid] - ints[0] < 0) {
                // l <= mid < r && unique && sort(d) && mid = (l + r) / 2 && ints[mid] - ints[0] < 0
                // && (l == 0 || ints[l - 1] < ints[l]) && (r >= n - 1 || ints[r + 1] > ints[r]) &&
                // (ints[l] - ints[0] >= 0 && ints[r] - ints[0] < 0 || ints[l] - ints[0] >= 0 && ints[r] - ints[0] > 0)

                // l <= mid < r && unique && sort(d) && mid = (l + r) / 2 && ints[mid] - ints[0] < 0
                // && (l == 0 || ints[l - 1] < ints[l]) && (r >= n - 1 || ints[r + 1] > ints[r]) &&
                // ints[l] - ints[0] >= 0 && ints[r] - ints[0] < 0

                // l <= mid < r && unique && sort(d) && mid = (l + r) / 2
                // && (l == 0 || ints[l - 1] < ints[l]) && (r >= n - 1 || ints[r + 1] > ints[r])
                // && ints[mid] < ints[r] && ints[l] - ints[0] >= 0 && ints[mid] - ints[0] < 0
                r = mid;
                // l <= mid == r && unique && sort(d)
                // && (l == 0 || ints[l - 1] < ints[l]) && (r >= n - 1 || ints[r + 1] > ints[r])
                // && ints[l] - ints[0] >= 0 && ints[r] - ints[0] < 0
            } else {
                // l <= mid < r && unique && sort(d) && mid = (l + r) / 2 && ints[mid] - ints[0] >= 0 &&
                // ints[mid] >= ints[l] && (l == 0 || ints[l - 1] < ints[l]) && (r >= n - 1 || ints[r + 1] > ints[r]) &&
                // (ints[l] - ints[0] >= 0 && ints[r] - ints[0] < 0 || ints[l] - ints[0] >= 0 && ints[r] - ints[0] > 0)

                // l <= mid < r && unique && sort(d) && mid = (l + r) / 2 && ints[mid] - ints[0] >= 0
                // && (l == 0 || ints[l - 1] < ints[l]) && (r >= n - 1 || ints[r + 1] > ints[r]) &&
                // (ints[l] - ints[0] >= 0 && ints[r] - ints[0] < 0 || ints[l] - ints[0] >= 0 && ints[r] - ints[0] > 0)

                // l <= mid < r && unique && sort(d) && mid = (l + r) / 2 && ints[mid] > ints[l]
                // && (l == 0 || ints[l - 1] < ints[l]) && (r >= n - 1|| ints[r + 1] > ints[r]) &&
                // (ints[mid] - ints[0] >= 0 && ints[r] - ints[0] < 0 ||
                // ints[mid] - ints[0] >= 0 && ints[r] - ints[0] > 0)
                l = mid + 1;
                // l <= r && l == mid + 1  && unique && sort(d) &&
                // && (l == 0 || ints[l - 1] < ints[l]) && (r >= n- 1 || ints[r + 1] > ints[r])
                // (ints[l] - ints[0] > 0 && ints[r] - ints[0] < 0 ||
                // ints[l] - ints[0] > 0 && ints[r] - ints[0] > 0)
            }
            // l <= r && unique && sort(d)
            // && (l == 0 || ints[l - 1] < ints[l]) && (r >= n - 1 || ints[r + 1] > ints[r]) &&
            // (ints[l] - ints[0] > 0 && ints[r] - ints[0] < 0 || ints[l] - ints[0] >= 0 && ints[r] - ints[0] > 0)
        }
        // unique && sort(d) && l == r
        // && (l == 0 || ints[l - 1] < ints[l]) && (r >= n - 1  || ints[r + 1] > ints[r]) &&
        // (ints[l] - ints[0] > 0 && ints[r] - ints[0] < 0 || ints[l] - ints[0] >= 0 && ints[r] - ints[0] > 0)

        // unique && sort(d) && l == r
        // ints[l] == ints[r] >= ints[0] && (l == 0 || l == n  || (ints[l] > ints[0] && ints[l - 1] < ints[0]))
        return l;
    }
    // ints[rez] >= ints[0] && (l == 0 || l == n  || (ints[l] > ints[0] && ints[l - 1] < ints[0]))


    // Inv: l <= r && unique && sort(d) && (l == 0 || ints[l - 1] < ints[l]) && (r == n - 1 || ints[r + 1] > ints[r])
    // (ints[l] - ints[0] >= 0 && ints[r] - ints[0] < 0 || ints[l] - ints[0] >= 0 && ints[r] - ints[0] > 0)
    public static Integer binSearchStartRecursion(final int[] ints, int l, int r) {
        // l <= r && unique && sort(d) && (l == 0 || ints[l - 1] < ints[l]) && (r == n - 1 || ints[r + 1] > ints[r])
        // (ints[l] - ints[0] >= 0 && ints[r] - ints[0] < 0 || ints[l] - ints[0] >= 0 && ints[r] - ints[0] > 0)
        if (l == r) {
            // unique && sort(d) && l == r
            // && (l == 0 || ints[l - 1] < ints[l]) && (r == n - 1 || ints[r + 1] > ints[r]) &&
            // (ints[l] - ints[0] > 0 && ints[r] - ints[0] < 0 || ints[l] - ints[0] >= 0 && ints[r] - ints[0] > 0)

            // unique && sort(d) && l == r
            // ints[l] == ints[r] >= ints[0] && (l == 0 || r == n - 1 || (ints[r + 1] > ints[r] && ints[l - 1] < ints[l]))
            return r;
        }
        // l < r && unique && sort(d) && (ints[l] - ints[0] <= 0)
        // && (l == 0 || ints[l - 1] < ints[l]) && (r == n - 1 || ints[r + 1] > ints[r]) &&
        // (ints[l] - ints[0] >= 0 && ints[r] - ints[0] < 0 || ints[l] - ints[0] >= 0 && ints[r] - ints[0] > 0)
        int mid = (l + r) / 2;
        // l <= mid < r && unique && sort(d) && mid = (l + r) / 2
        // && (l == 0 || ints[l - 1] < ints[l]) && (r == n - 1 || ints[r + 1] > ints[r]) &&
        // (ints[l] - ints[0] >= 0 && ints[r] - ints[0] < 0 || ints[l] - ints[0] >= 0 && ints[r] - ints[0] > 0)
        if (ints[mid] - ints[0] < 0) {
            // l <= mid < r && unique && sort(d) && mid = (l + r) / 2 && ints[mid] - ints[0] < 0
            // && (l == 0 || ints[l - 1] < ints[l]) && (r == n - 1 || ints[r + 1] > ints[r]) &&
            // (ints[l] - ints[0] >= 0 && ints[r] - ints[0] < 0 || ints[l] - ints[0] >= 0 && ints[r] - ints[0] > 0)

            // l <= mid < r && unique && sort(d) && mid = (l + r) / 2 && ints[mid] - ints[0] < 0
            // && (l == 0 || ints[l - 1] < ints[l]) && (r == n - 1 || ints[r + 1] > ints[r]) &&
            // ints[l] - ints[0] >= 0 && ints[r] - ints[0] < 0

            // l <= mid < r && unique && sort(d) && mid = (l + r) / 2
            // && (l == 0 || ints[l - 1] < ints[l]) && (r == n - 1 || ints[r + 1] > ints[r])
            // && ints[mid] < ints[r] && ints[l] - ints[0] >= 0 && ints[mid] - ints[0] < 0
            return binSearchStartRecursion(ints, l, mid);
            // l <= mid == r && unique && sort(d) && mid = (l + r) / 2
            // && (l == 0 || ints[l - 1] < ints[l]) && (r == n - 1 || ints[r + 1] > ints[r])
            // && ints[l] - ints[0] >= 0 && ints[r] - ints[0] < 0
        } else {
            // l <= mid < r && unique && sort(d) && mid = (l + r) / 2 && ints[mid] - ints[0] >= 0 &&
            // ints[mid] >= ints[l] && (l == 0 || ints[l - 1] < ints[l]) && (r == n - 1 || ints[r + 1] > ints[r]) &&
            // (ints[l] - ints[0] >= 0 && ints[r] - ints[0] < 0 || ints[l] - ints[0] >= 0 && ints[r] - ints[0] > 0)

            // l <= mid < r && unique && sort(d) && mid = (l + r) / 2 && ints[mid] - ints[0] >= 0
            // && (l == 0 || ints[l - 1] < ints[l]) && (r == n - 1 || ints[r + 1] > ints[r]) &&
            // (ints[l] - ints[0] >= 0 && ints[r] - ints[0] < 0 || ints[l] - ints[0] >= 0 && ints[r] - ints[0] > 0)

            // l <= mid < r && unique && sort(d) && mid = (l + r) / 2
            // && (l == 0 || ints[l - 1] < ints[l]) && (r == n - 1 || ints[r + 1] > ints[r]) &&
            // (ints[mid] - ints[0] >= 0 && ints[r] - ints[0] < 0 ints[mid] - ints[0] >= 0 && ints[r] - ints[0] > 0)
            return binSearchStartRecursion(ints, mid + 1, r);
            // l <= r && l == mid + 1  && unique && sort(d) &&
            // && (l == 0 || ints[l - 1] < ints[l]) && (r == n - 1 || ints[r + 1] > ints[r])
            // (ints[l] - ints[0] > 0 && ints[r] - ints[0] < 0 || ints[l] - ints[0] > 0 && ints[r] - ints[0] > 0)
        }
    }

    // for i = 1... args.length - 1: Integer.parseInt(args[0]) + sort(d) && Integer.parseInt(args[0])
    public static void main(String[] args) {
        // for i = 1... n - 1: Integer.parseInt(args[0]) + sort(d)
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
                // args.length > 0 && x == Integer.parseInt(args[0]) && i == i'
                // i < args.length

                // args.length > 0 && x == Integer.parseInt(args[0]) && i = i' + 1
            }
            // args.length > 0 && x == Integer.parseInt(args[0]) && sort(d) && unique
            int d = binSearchStartIteration(nums);
            // ints[rez] >= ints[0] && (l == 0 || l == n  || (ints[l] > ints[0] && ints[l - 1] < ints[0]))  && unique
            // sort(d) && unique
            System.out.println(binSearchIteration(x, nums, d));
            // unique && sort(d) && (ints[rez] == x || rez == -1)

            // args.length > 0 && x == Integer.parseInt(args[0]) && sort(d) && unique
            //int d = binSearchStartRecursion(nums, 0, nums.length);
            // ints[rez] >= ints[0] && (l == 0 || l == n  || (ints[l] > ints[0] && ints[l - 1] < ints[0]))  && unique
            // sort(d) && unique
            //System.out.println(binSearchRecursion(x, nums, 0, nums.length - 1, d));
            // unique && sort(d) && (ints[rez] == x || rez == -1)
        } else {
            System.err.println("Too few arguments");
        }
    }
}